package com.jeesite.modules.blog.admin.annotion.AuthorityVerify;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.internal.LinkedTreeMap;
import com.jeesite.modules.blog.admin.global.MessageConf;
import com.jeesite.modules.blog.admin.global.RedisConf;
import com.jeesite.modules.blog.admin.global.SQLConf;
import com.jeesite.modules.blog.admin.global.SysConf;
import com.jeesite.modules.blog.base.enums.EMenuType;
import com.jeesite.modules.blog.base.enums.EStatus;
import com.jeesite.modules.blog.base.global.ECode;
import com.jeesite.modules.blog.commons.entity.Admin;
import com.jeesite.modules.blog.commons.entity.CategoryMenu;
import com.jeesite.modules.blog.commons.entity.Role;
import com.jeesite.modules.blog.utils.JsonUtils;
import com.jeesite.modules.blog.utils.RedisUtil;
import com.jeesite.modules.blog.utils.ResultUtil;
import com.jeesite.modules.blog.utils.StringUtils;
import com.jeesite.modules.blog.xo.service.AdminService;
import com.jeesite.modules.blog.xo.service.CategoryMenuService;
import com.jeesite.modules.blog.xo.service.RoleService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 权限校验 切面实现
 *
 * @author: 陌溪
 * @create: 2020-03-06-19:05
 */
@Aspect
@Component

public class AuthorityVerifyAspect {
    private static Logger log = LoggerFactory.getLogger(AuthorityVerifyAspect.class);

    @Autowired
    CategoryMenuService categoryMenuService;

    @Autowired
    RoleService roleService;

    @Autowired
    AdminService adminService;

    @Autowired
    RedisUtil redisUtil;

    @Pointcut(value = "@annotation(authorityVerify)")
    public void pointcut(AuthorityVerify authorityVerify) {

    }

    @Around(value = "pointcut(authorityVerify)")
    public Object doAround(ProceedingJoinPoint joinPoint, AuthorityVerify authorityVerify) throws Throwable {

        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attribute.getRequest();

        //获取请求路径
        String url = request.getRequestURI();

        // 解析出请求者的ID和用户名
        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();

        // 管理员能够访问的路径
        String visitUrlStr = redisUtil.get(RedisConf.ADMIN_VISIT_MENU + RedisConf.SEGMENTATION + adminUid);

        LinkedTreeMap<String, String> visitMap = new LinkedTreeMap<>();

        if (StringUtils.isNotEmpty(visitUrlStr)) {
            // 从Redis中获取
            visitMap = (LinkedTreeMap<String, String>) JsonUtils.jsonToMap(visitUrlStr, String.class);
        } else {
            // 查询数据库获取
            Admin admin = adminService.getById(adminUid);

            String roleUid = admin.getRoleUid();

            Role role = roleService.getById(roleUid);

            String caetgoryMenuUids = role.getCategoryMenuUids();

            String[] uids = caetgoryMenuUids.replace("[", "").replace("]", "").replace("\"", "").split(",");

            List<String> categoryMenuUids = new ArrayList<>(Arrays.asList(uids));

            // 这里只需要查询访问的按钮
            QueryWrapper<CategoryMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.in(SQLConf.UID, categoryMenuUids);
            queryWrapper.eq(SQLConf.MENU_TYPE, EMenuType.BUTTON);
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            List<CategoryMenu> buttonList = categoryMenuService.list(queryWrapper);

            for (CategoryMenu item : buttonList) {
                if (StringUtils.isNotEmpty(item.getUrl())) {
                    visitMap.put(item.getUrl(), item.getUrl());
                }
            }
            // 将访问URL存储到Redis中
            redisUtil.setEx(RedisConf.ADMIN_VISIT_MENU + SysConf.REDIS_SEGMENTATION + adminUid, JsonUtils.objectToJson(visitMap), 1, TimeUnit.HOURS);
        }

        // 判断该角色是否能够访问该接口
        if (visitMap.get(url) != null) {
            log.info("用户拥有操作权限，访问的路径: {}，拥有的权限接口：{}", url, visitMap.get(url));
            //执行业务
            return joinPoint.proceed();
        } else {
            log.info("用户不具有操作权限，访问的路径: {}", url);
            return ResultUtil.result(ECode.NO_OPERATION_AUTHORITY, MessageConf.RESTAPI_NO_PRIVILEGE);
        }
    }

}
