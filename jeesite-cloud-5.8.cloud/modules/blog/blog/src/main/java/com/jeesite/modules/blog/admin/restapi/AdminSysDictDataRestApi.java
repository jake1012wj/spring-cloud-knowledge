package com.jeesite.modules.blog.admin.restapi;


import com.jeesite.modules.blog.admin.annotion.AuthorityVerify.AuthorityVerify;
import com.jeesite.modules.blog.admin.annotion.AvoidRepeatableCommit.AvoidRepeatableCommit;
import com.jeesite.modules.blog.admin.annotion.OperationLogger.OperationLogger;
import com.jeesite.modules.blog.admin.global.MessageConf;
import com.jeesite.modules.blog.admin.global.SysConf;
import com.jeesite.modules.blog.base.exception.ThrowableUtils;
import com.jeesite.modules.blog.base.validator.group.Delete;
import com.jeesite.modules.blog.base.validator.group.GetList;
import com.jeesite.modules.blog.base.validator.group.Insert;
import com.jeesite.modules.blog.base.validator.group.Update;
import com.jeesite.modules.blog.utils.ResultUtil;
import com.jeesite.modules.blog.utils.StringUtils;
import com.jeesite.modules.blog.xo.service.SysDictDataService;
import com.jeesite.modules.blog.xo.vo.SysDictDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 字典数据 RestApi
 *
 * @author 陌溪
 * @date 2020年2月15日21:16:31
 */
@RestController
@RequestMapping("/sysDictData")
@Api(value = "字典数据相关接口", tags = {"字典数据相关接口"})

public class AdminSysDictDataRestApi {
    private static Logger log = LoggerFactory.getLogger(AdminSysDictDataRestApi.class);
    @Autowired
    private SysDictDataService sysDictDataService;

    @AuthorityVerify
    @ApiOperation(value = "获取字典数据列表", notes = "获取字典数据列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody SysDictDataVO sysDictDataVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("获取字典数据列表");
        return ResultUtil.successWithData(sysDictDataService.getPageList(sysDictDataVO));
    }

    @AvoidRepeatableCommit
    @AuthorityVerify
    @OperationLogger(value = "增加字典数据")
    @ApiOperation(value = "增加字典数据", notes = "增加字典数据", response = String.class)
    @PostMapping("/add")
    public String add(HttpServletRequest request, @Validated({Insert.class}) @RequestBody SysDictDataVO sysDictDataVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return sysDictDataService.addSysDictData(sysDictDataVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "编辑字典数据")
    @ApiOperation(value = "编辑字典数据", notes = "编辑字典数据", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request, @Validated({Update.class}) @RequestBody SysDictDataVO sysDictDataVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return sysDictDataService.editSysDictData(sysDictDataVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "批量删除字典数据")
    @ApiOperation(value = "批量删除字典数据", notes = "批量删除字典数据", response = String.class)
    @PostMapping("/deleteBatch")
    public String delete(HttpServletRequest request, @Validated({Delete.class}) @RequestBody List<SysDictDataVO> sysDictDataVoList, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return sysDictDataService.deleteBatchSysDictData(sysDictDataVoList);
    }

    @ApiOperation(value = "根据字典类型获取字典数据", notes = "根据字典类型获取字典数据", response = String.class)
    @PostMapping("/getListByDictType")
    public String getListByDictType(@RequestParam("dictType") String dictType) {
        if (StringUtils.isEmpty(dictType)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        }
        return ResultUtil.result(SysConf.SUCCESS, sysDictDataService.getListByDictType(dictType));
    }

    @ApiOperation(value = "根据字典类型数组获取字典数据", notes = "根据字典类型数组获取字典数据", response = String.class)
    @PostMapping("/getListByDictTypeList")
    public String getListByDictTypeList(@RequestBody List<String> dictTypeList) {

        if (dictTypeList.size() <= 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        }
        return ResultUtil.result(SysConf.SUCCESS, sysDictDataService.getListByDictTypeList(dictTypeList));
    }

}

