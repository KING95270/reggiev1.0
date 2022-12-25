package com.example.reggie.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.currency.R;
import com.example.reggie.entity.Category;
import com.example.reggie.entity.Dish;
import com.example.reggie.entity.Setmeal;
import com.example.reggie.mapper.CategoryMapper;
import com.example.reggie.service.CategoryService;
import com.example.reggie.service.DishService;
import com.example.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmealService;


    @Override
    public R<String> delete(Long id) {
        //定义条件
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);

        //根据条件查询统计数量
        int dishCount = dishService.count(dishLambdaQueryWrapper);

        //定义条件  根据xxx查询 条件是xxx ==      Setmeal::getCategoryId == id
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);

        int SetmealCount = setmealService.count(setmealLambdaQueryWrapper);

        if (dishCount > 0) {
            //抛异常 该xx中存在数据 不允许删除
            return R.error("删除失败" + dishCount);
        } else if (SetmealCount > 0) {
            //抛异常 该xx中存在数据 不允许删除
            return R.error("删除失败,该套餐中存在数据："+SetmealCount+"条");
        } else {
            //该xx中不存在数据 允许删除
            super.removeById(id);
            return R.success("删除成功");
        }


    }
}
