package com.smic.cf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smic.cf.entity.MpUser;

@Mapper
public interface UserMapper extends BaseMapper<MpUser> {
	
	/*
	 * 自定义SQL练习
	 */
//	@Select("select * from mp_user ${ew.customSqlSegment}")
	List<MpUser> selectAll(@Param(Constants.WRAPPER) Wrapper<MpUser> wrapper);
	
	IPage<MpUser> selectUserPage(Page<MpUser> page,@Param(Constants.WRAPPER) Wrapper<MpUser> wrapper);

}
