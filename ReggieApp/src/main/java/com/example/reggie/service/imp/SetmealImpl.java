package com.example.reggie.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.currency.R;
import com.example.reggie.dto.SetmealDto;
import com.example.reggie.entity.Setmeal;
import com.example.reggie.entity.SetmealDish;
import com.example.reggie.mapper.SetmealMapper;
import com.example.reggie.service.SetMealDishService;
import com.example.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SetmealImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    SetMealDishService setMealDishService;

    @Transactional
    @Override
    public R<String> saveAll(SetmealDto dto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(dto, setmeal, "setmealDishes");
        save(setmeal);

        List<SetmealDish> setmealDishes = dto.getSetmealDishes();

        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmeal.getId());
            return item;
        }).collect(Collectors.toList());

        setMealDishService.saveBatch(setmealDishes);

        return R.success("成功");
    }

    @Override
    public R<SetmealDto> Id(Long id) {

        //创建dto对象
        SetmealDto dto = new SetmealDto();

        //根据id查询
        Setmeal byId = getById(id);

        //把数据复制到dto
        BeanUtils.copyProperties(byId, dto);

        //创建条件
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);

        //根据SetmealId查询套餐中的菜品
        List<SetmealDish> list = setMealDishService.list(dishLambdaQueryWrapper);

        //存入
        dto.setSetmealDishes(list);

        return R.success(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void Put(SetmealDto dto) {
        //修改套餐
        updateById(dto);
        //保存套餐菜品

        /**
         * 采用先删后增方法
         */
        //根据id删除所有菜品
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, dto.getId());
        setMealDishService.remove(dishLambdaQueryWrapper);

        //对SetmealId进行赋值
        List<SetmealDish> setmealDishes = dto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(dto.getId());
            return item;
        }).collect(Collectors.toList());

        //增
        setMealDishService.saveBatch(setmealDishes);
    }

    /**
     * 批量删除
     * @param list id
     */
    @Transactional
    @Override
    public void deleteAll(List<Long> list) {
        //删除套餐表

        for (int i = 0; i < list.size(); i++) {
            LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealLambdaQueryWrapper.eq(Setmeal::getId,list.get(i));
            remove(setmealLambdaQueryWrapper);
        }

        //删除套餐菜品
        for (int i = 0; i < list.size(); i++) {
            LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,list.get(i));
            setMealDishService.remove(dishLambdaQueryWrapper);
        }


    }
}
