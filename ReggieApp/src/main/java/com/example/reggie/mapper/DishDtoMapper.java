package com.example.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggie.dto.DishDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishDtoMapper extends BaseMapper<DishDto> {
}
