package com.example.reggie.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.entity.User;
import com.example.reggie.mapper.UserMapper;
import com.example.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
