package com.jeesite.modules.blog.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeesite.modules.blog.commons.entity.SysLog;
import com.jeesite.modules.blog.xo.vo.SysLogVO;
import com.jeesite.modules.blog.base.service.SuperService;

/**
 * 操作日志 服务类
 *
 * @author limbo
 * @date 2018-09-30
 */
public interface SysLogService extends SuperService<SysLog> {

    /**
     * 获取操作日志列表
     *
     * @param sysLogVO
     * @return
     */
    public IPage<SysLog> getPageList(SysLogVO sysLogVO);
}
