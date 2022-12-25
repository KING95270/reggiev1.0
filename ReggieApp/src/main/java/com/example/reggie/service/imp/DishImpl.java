package com.example.reggie.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.currency.R;
import com.example.reggie.dto.DishDto;
import com.example.reggie.entity.Dish;
import com.example.reggie.entity.DishFlavor;
import com.example.reggie.mapper.DishMapper;
import com.example.reggie.service.DishFlavorService;
import com.example.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;

    //添加菜品与口味
    @Transactional
    public void save(DishDto dishDto) {

        //存入菜品
        super.save(dishDto);

        //新菜品的id
        Long id = dishDto.getId();


        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        //存入口味
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public DishDto GetIdFlavor(Long id) {
        Dish byId = getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(byId, dishDto);

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, id);

        List<DishFlavor> list = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

        dishDto.setFlavors(list);

        return dishDto;
    }


    /**
     * 开始事务
     * 更新菜品与口味
     * @param dishDto
     */
    @Override
    @Transactional
    public void UpdateSave(DishDto dishDto) {

        //更新菜品信息表
        updateById(dishDto);

        //更新口味信息表
        //先删除，后新增，不管改没改

        LambdaQueryWrapper<DishFlavor> f = new LambdaQueryWrapper<>();
        f.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(f);
        List<DishFlavor> listF = dishDto.getFlavors();

        listF.stream().map((item) -> {

            item.setDishId(dishDto.getId());
            return item;

        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(listF);

    }

}
