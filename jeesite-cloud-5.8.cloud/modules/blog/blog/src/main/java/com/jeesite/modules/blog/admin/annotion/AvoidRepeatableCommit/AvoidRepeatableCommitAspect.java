package com.jeesite.modules.blog.admin.annotion.AvoidRepeatableCommit;

import com.jeesite.modules.blog.base.holder.RequestHolder;
import com.jeesite.modules.blog.utils.IpUtils;
import com.jeesite.modules.blog.utils.RedisUtil;
import com.jeesite.modules.blog.utils.ResultUtil;
import com.jeesite.modules.blog.utils.StringUtils;
import com.jeesite.modules.blog.xo.global.RedisConf;
import com.jeesite.modules.blog.xo.global.SysConf;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 避免接口重复提交AOP
 *
 * @author: 陌溪
 * @create: 2020-04-23-12:12
 */
@Aspect
@Component

public class AvoidRepeatableCommitAspect {
    private static Logger log = LoggerFactory.getLogger(AvoidRepeatableCommitAspect.class);
    @Autowired
    private RedisUtil redisUtil;

    /**
     * @param point
     */
    @Around("@annotation(com.jeesite.modules.blog.admin.annotion.AvoidRepeatableCommit.AvoidRepeatableCommit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        HttpServletRequest request = RequestHolder.getRequest();

        String ip = IpUtils.getIpAddr(request);

        //获取注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        //目标类、方法
        String className = method.getDeclaringClass().getName();

        String name = method.getName();

        // 得到类名和方法
        String ipKey = String.format("%s#%s", className, name);

        // 转换成HashCode
        int hashCode = Math.abs(ipKey.hashCode());

        String key = String.format("%s:%s_%d", RedisConf.AVOID_REPEATABLE_COMMIT, ip, hashCode);

        log.info("ipKey={},hashCode={},key={}", ipKey, hashCode, key);

        AvoidRepeatableCommit avoidRepeatableCommit = method.getAnnotation(AvoidRepeatableCommit.class);

        long timeout = avoidRepeatableCommit.timeout();

        String value = redisUtil.get(key);

        if (StringUtils.isNotBlank(value)) {
            log.info("请勿重复提交表单");
            return ResultUtil.result(SysConf.ERROR, "请勿重复提交表单");
        }

        // 设置过期时间
        redisUtil.setEx(key, StringUtils.getUUID(), timeout, TimeUnit.MILLISECONDS);

        //执行方法
        Object object = point.proceed();
        return object;
    }

}
