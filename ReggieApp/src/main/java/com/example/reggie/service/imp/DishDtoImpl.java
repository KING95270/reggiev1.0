package com.example.reggie.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.dto.DishDto;
import com.example.reggie.mapper.DishDtoMapper;
import com.example.reggie.service.DishDtoService;
import com.example.reggie.service.DishFlavorService;
import com.example.reggie.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishDtoImpl extends ServiceImpl<DishDtoMapper, DishDto> implements DishDtoService {
}
