package com.jeesite.modules.blog.web.annotion.log;

import com.jeesite.modules.blog.base.enums.EBehavior;
import com.jeesite.modules.blog.base.holder.RequestHolder;
import com.jeesite.modules.blog.base.util.RequestUtil;
import com.jeesite.modules.blog.utils.AopUtils;
import com.jeesite.modules.blog.utils.AspectUtil;
import com.jeesite.modules.blog.utils.IpUtils;
import com.jeesite.modules.blog.utils.RedisUtil;
import com.jeesite.modules.blog.web.global.SysConf;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 日志切面
 */
@Aspect
@Component("WebLoggerAspect")

public class LoggerAspect {
    private static Logger log = LoggerFactory.getLogger(LoggerAspect.class);
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    RedisUtil redisUtil;

    @Pointcut(value = "@annotation(bussinessLog)")
    public void pointcut(BussinessLog bussinessLog) {

    }

    @Around(value = "pointcut(bussinessLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, BussinessLog bussinessLog) throws Throwable {

        //先执行业务
        Object result = joinPoint.proceed();

        try {
            // 日志收集
            handle(joinPoint);

        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        return result;
    }

    private void handle(ProceedingJoinPoint point) throws Exception {

        HttpServletRequest request = RequestHolder.getRequest();

        Method currentMethod = AspectUtil.INSTANCE.getMethod(point);
        //获取操作名称
        BussinessLog annotation = currentMethod.getAnnotation(BussinessLog.class);

        boolean save = annotation.save();

        EBehavior behavior = annotation.behavior();

        String bussinessName = AspectUtil.INSTANCE.parseParams(point.getArgs(), annotation.value());

        String ua = RequestUtil.getUa();

        log.info("{} | {} - {} {} - {}", bussinessName, IpUtils.getIpAddr(request), RequestUtil.getMethod(), RequestUtil.getRequestUrl(), ua);
        if (!save) {
            return;
        }

        // 获取参数名称和值
        Map<String, Object> nameAndArgsMap = AopUtils.getFieldsName(point);

        Map<String, String> result = EBehavior.getModuleAndOtherData(behavior, nameAndArgsMap, bussinessName);

        if (result != null) {
            String userUid = "";
            if (request.getAttribute(SysConf.USER_UID) != null) {
                userUid = request.getAttribute(SysConf.USER_UID).toString();
            }

            Map<String, String> map = IpUtils.getOsAndBrowserInfo(request);
            String os = map.get(SysConf.OS);
            String browser = map.get(SysConf.BROWSER);
            String ip = IpUtils.getIpAddr(request);
            // 异步存储日志
            threadPoolTaskExecutor.execute(
                    new SysLogHandle(userUid, ip, os, browser,
                            behavior.getBehavior(),
                            result.get(SysConf.MODULE_UID),
                            result.get(SysConf.OTHER_DATA), redisUtil));
        }
    }
}
