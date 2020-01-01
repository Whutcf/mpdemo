package com.smic.cf.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smic.cf.dao.UserMapper;
import com.smic.cf.entity.MpUser;
import com.smic.cf.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, MpUser> implements UserService {

}
