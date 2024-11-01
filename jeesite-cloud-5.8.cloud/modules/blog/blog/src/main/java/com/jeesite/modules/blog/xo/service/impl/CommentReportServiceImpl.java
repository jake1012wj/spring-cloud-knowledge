package com.jeesite.modules.blog.xo.service.impl;

import com.jeesite.modules.blog.commons.entity.CommentReport;
import com.jeesite.modules.blog.xo.mapper.CommentReportMapper;
import com.jeesite.modules.blog.xo.service.CommentReportService;
import com.jeesite.modules.blog.base.serviceImpl.SuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 评论举报表 服务实现类
 *
 * @author 陌溪
 * @date 2020年1月12日15:47:47
 */
@Service
public class CommentReportServiceImpl extends SuperServiceImpl<CommentReportMapper, CommentReport> implements CommentReportService {

}
