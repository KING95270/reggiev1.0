package com.example.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie.currency.R;
import com.example.reggie.dto.DishDto;
import com.example.reggie.entity.Dish;
import org.apache.ibatis.annotations.Select;

public interface DishService extends IService<Dish> {
    public void save(DishDto dishDto);


    //根据id查询菜品信息与口味信息
    DishDto GetIdFlavor(Long id);


    //更新菜品信息，同时更新口味信息
    public void UpdateSave(DishDto dishDto);

}
