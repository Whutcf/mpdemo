package com.smic.cf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smic.cf.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
	
	/*
	 * 自定义SQL练习
	 */
//	@Select("select * from mp_user ${ew.customSqlSegment}")
	List<User> selectAll(@Param(Constants.WRAPPER) Wrapper<User> wrapper);
	
	IPage<User> selectUserPage(Page<User> page,@Param(Constants.WRAPPER) Wrapper<User> wrapper);

}
