package com.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie.currency.R;
import com.example.reggie.dto.SetmealDto;
import com.example.reggie.entity.Setmeal;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {

    //保存套餐与菜品
    R<String> saveAll(SetmealDto dto);

    //修改数据回显
    R<SetmealDto> Id(Long id);

    //修改 ---> 保存
    void Put(SetmealDto dto);

    //批量删除
    void deleteAll(List<Long> list);
}
