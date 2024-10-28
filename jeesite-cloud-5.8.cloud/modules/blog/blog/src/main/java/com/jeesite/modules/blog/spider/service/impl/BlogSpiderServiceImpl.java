package com.jeesite.modules.blog.spider.service.impl;


import com.jeesite.modules.blog.spider.entity.BlogSpider;
import com.jeesite.modules.blog.spider.mapper.BlogSpiderMapper;
import com.jeesite.modules.blog.spider.service.BlogSpiderService;
import com.jeesite.modules.blog.base.serviceImpl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博客爬取服务实现类
 * </p>
 *
 * @author 陌溪
 * @since 2020年2月7日21:29:42
 */

@Service
public class BlogSpiderServiceImpl extends SuperServiceImpl<BlogSpiderMapper, BlogSpider> implements BlogSpiderService {

}
