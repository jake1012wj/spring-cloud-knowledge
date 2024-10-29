package com.jeesite.modules.blog.commons.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;


//@Component
//public class MetaObjectHandlerConfig implements MetaObjectHandler {
//    private static Logger log = LoggerFactory.getLogger(MetaObjectHandlerConfig.class);
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        log.info("插入方法填充");
//        setFieldValByName("createTime", new Date(), metaObject);
//        setFieldValByName("updateTime", new Date(), metaObject);
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        log.info("更新方法填充");
//        setFieldValByName("updateTime", new Date(), metaObject);
//    }
//}
