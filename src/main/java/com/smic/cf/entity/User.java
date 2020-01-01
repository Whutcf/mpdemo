package com.smic.cf.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class User {

	//主键
	@TableId(type=IdType.AUTO)
	private Long userId;
	
	//姓名
	@TableField("name")
	private String realName;
	
	//年龄
	private Integer age;
	
	//邮箱
	private String email;
	
	//直属上级
	private Long managerId;
	
	//创建时间
	private LocalDateTime createTime;
	
	//备注 临时字段
	@TableField(exist=false)
	private String remark;
}
