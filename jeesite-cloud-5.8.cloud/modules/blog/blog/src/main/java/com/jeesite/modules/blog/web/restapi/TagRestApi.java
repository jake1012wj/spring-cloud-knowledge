package com.jeesite.modules.blog.web.restapi;


import com.jeesite.modules.blog.base.enums.EBehavior;
import com.jeesite.modules.blog.utils.ResultUtil;
import com.jeesite.modules.blog.utils.StringUtils;
import com.jeesite.modules.blog.web.annotion.log.BussinessLog;
import com.jeesite.modules.blog.web.global.SysConf;
import com.jeesite.modules.blog.xo.service.BlogService;
import com.jeesite.modules.blog.xo.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 标签RestApi
 *
 * @author 陌溪
 * @date 2020年4月26日12:03:17
 */
@RestController
@RequestMapping("/tag")
@Api(value = "博客标签相关接口", tags = {"博客标签相关接口"})

public class TagRestApi {
    private static Logger log = LoggerFactory.getLogger(TagRestApi.class);
    @Autowired
    private BlogService blogService;
    @Autowired
    private TagService tagService;

    /**
     * 获取标签的信息
     *
     * @return
     */
    @ApiOperation(value = "获取标签的信息", notes = "获取标签的信息")
    @GetMapping("/getTagList")
    public String getTagList() {
        log.info("获取标签信息");
        return ResultUtil.result(SysConf.SUCCESS, tagService.getList());
    }

    /**
     * 通过TagUid获取文章
     *
     * @param request
     * @param currentPage
     * @param pageSize
     * @return
     */
    @BussinessLog(value = "点击标签", behavior = EBehavior.VISIT_TAG)
    @ApiOperation(value = "通过TagUid获取文章", notes = "通过TagUid获取文章")
    @GetMapping("/getArticleByTagUid")
    public String getArticleByTagUid(HttpServletRequest request,
                                     @ApiParam(name = "tagUid", value = "标签UID", required = false) @RequestParam(name = "tagUid", required = false) String tagUid,
                                     @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                                     @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {

        if (StringUtils.isEmpty(tagUid)) {
            return ResultUtil.result(SysConf.ERROR, "传入TagUid不能为空");
        }
        log.info("通过blogSortUid获取文章列表");
        return ResultUtil.result(SysConf.SUCCESS, blogService.searchBlogByTag(tagUid, currentPage, pageSize));
    }

}

