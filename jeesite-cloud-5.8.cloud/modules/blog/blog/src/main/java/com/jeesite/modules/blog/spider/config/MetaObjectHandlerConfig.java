package com.jeesite.modules.blog.spider.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jeesite.modules.blog.spider.global.SysConf;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component

//public class MetaObjectHandlerConfig implements MetaObjectHandler {
//    private static Logger log = LoggerFactory.getLogger(MetaObjectHandlerConfig.class);
//
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        log.info("插入方法填充");
//        setFieldValByName(SysConf.CREATE_TIME, new Date(), metaObject);
//        setFieldValByName(SysConf.UPDATE_TIME, new Date(), metaObject);
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        log.info("更新方法填充");
//        setFieldValByName(SysConf.UPDATE_TIME, new Date(), metaObject);
//    }
//}
