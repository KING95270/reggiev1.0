package com.example.reggie.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.entity.OrderDetail;
import com.example.reggie.mapper.OrderDetailMapper;
import com.example.reggie.service.OrderDetailService;
import com.example.reggie.service.OrdersService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
