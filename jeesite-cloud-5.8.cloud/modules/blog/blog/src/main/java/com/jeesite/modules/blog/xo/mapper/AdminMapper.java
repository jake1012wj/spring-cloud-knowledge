package com.jeesite.modules.blog.xo.mapper;

import com.jeesite.modules.blog.commons.entity.Admin;
import com.jeesite.modules.blog.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 管理员表 Mapper 接口
 *
 * @author 陌溪
 * @since 2018-09-04
 */
public interface AdminMapper extends SuperMapper<Admin> {

    /**
     * 通过uid获取管理员
     *
     * @return
     */
    public Admin getAdminByUid(@Param("uid") String uid);
}
