package com.example.reggie.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.entity.SetmealDish;
import com.example.reggie.mapper.SetMealDishMapper;
import com.example.reggie.service.SetMealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SetMealDishImpl extends ServiceImpl<SetMealDishMapper, SetmealDish> implements SetMealDishService {
}
