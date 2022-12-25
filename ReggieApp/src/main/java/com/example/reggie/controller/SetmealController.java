package com.example.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.currency.R;
import com.example.reggie.dto.SetmealDto;
import com.example.reggie.entity.Setmeal;
import com.example.reggie.service.SetMealDishService;
import com.example.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    @Autowired
    SetMealDishService setMealDishService;

    /**
     * 查询所有
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        Page p = new Page(page, pageSize);

        LambdaUpdateWrapper<Setmeal> wrapper = new LambdaUpdateWrapper<>();
        wrapper.like(name != null, Setmeal::getName, name);

        setmealService.page(p, wrapper);

        return R.success(p);
    }

    @PostMapping()
    public R<String> save(@RequestBody SetmealDto dto) {
        R<String> stringR = setmealService.saveAll(dto);
        return stringR;
    }

    //修改回显数据
    @GetMapping("/{id}")
    public R<SetmealDto> GetId(@PathVariable Long id) {
        R<SetmealDto> id1 = setmealService.Id(id);
        return id1;
    }

    //修改 --> 保存
    @PutMapping
    public R<String> put(@RequestBody SetmealDto dto) {

        String msg;

        try {
            setmealService.Put(dto);
            msg = "成功";
        }catch (Exception e){
            msg = "失败";
        }
        return R.success(msg);
    }

    //批量删除
    @DeleteMapping()
    public R<String> deleteAll(Long[] ids){
        List<Long> list = Arrays.asList(ids);
        setmealService.deleteAll(list);
        return R.success("成功");
    }


    //批量停售
    @PostMapping("/status/{id}")
    public R<String> Status(@PathVariable int id,Long[] ids){

        List<Long> list = Arrays.asList(ids);

        LambdaUpdateWrapper<Setmeal> setmealLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        setmealLambdaUpdateWrapper.set(Setmeal::getStatus,id).in(Setmeal::getId,list);

        setmealService.update(setmealLambdaUpdateWrapper);

        return R.success("成功");
    }

    //手机端首页查询套餐
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId()).eq(Setmeal::getStatus,setmeal.getStatus());

        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);
        System.out.println(list);

        return R.success(list);
    }

}
