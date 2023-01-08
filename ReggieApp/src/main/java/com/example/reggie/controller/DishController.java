package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.currency.R;
import com.example.reggie.dto.DishDto;
import com.example.reggie.dto.SetmealDto;
import com.example.reggie.entity.Category;
import com.example.reggie.entity.Dish;
import com.example.reggie.entity.DishFlavor;
import com.example.reggie.entity.SetmealDish;
import com.example.reggie.service.CategoryService;
import com.example.reggie.service.DishDtoService;
import com.example.reggie.service.DishFlavorService;
import com.example.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    DishDtoService dtoService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DishFlavorService flavorService;

    @Autowired
    RedisTemplate redisTemplate;

    //查询所有菜品
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        //菜品管理
        //按名称进行模糊查找
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Dish::getName, name);

        //分页查询
        Page page1 = new Page(page, pageSize);
        dishService.page(page1, wrapper);


        Page<DishDto> dtoPage = new Page<>();
        //对象拷贝    元素 拷贝到...    排除掉xxx属性不拷
        BeanUtils.copyProperties(page1, dtoPage, "records");

        List<Dish> records = page1.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            //数据拷贝
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = dishDto.getCategoryId();

            Category byId = categoryService.getById(categoryId);
            String name1 = byId.getName();
            dishDto.setCategoryName(name1);

            return dishDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    //添加菜品
    @PostMapping
    public R<String> insert(@RequestBody DishDto dishFlavor) {
        dishService.save(dishFlavor);
        return R.success("成功");
    }

    //根据id查询
    @GetMapping("/{id}")
    public R<DishDto> selectId(@PathVariable Long id) {

        DishDto dishDto = dishService.GetIdFlavor(id);

        return R.success(dishDto);
    }

    @DeleteMapping
    public R<String> delete(Long ids) {


        dishService.removeById(ids);


        return R.success("成功");
    }

    @PutMapping
    public R<String> Put(@RequestBody DishDto dishDto) {

        dishService.UpdateSave(dishDto);


        return R.success("成功");
    }

    //起售 停售
    @PostMapping("/status/{id}")
    public R<String> Status(@PathVariable int id, Long[] ids) {

        //把多个id存入list
        List<Long> list = Arrays.asList(ids);

        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();

        //构造条件
        wrapper.set(Dish::getStatus, id).in(Dish::getId, list);

        dishService.update(wrapper);

        LambdaUpdateWrapper<Dish> wrapper1 = new LambdaUpdateWrapper<>();
        wrapper1.eq(Dish::getId,list.get(0));

        Dish one = dishService.getOne(wrapper1);


        List<DishDto> dishDtoList = null;

        String key = "dish_" + one.getCategoryId() + "_1";

        dishDtoList =(List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtoList != null){
            redisTemplate.delete(key);
        }

        return R.success("成功");
    }


    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {

        List<DishDto> dishDtoList = null;

        //生成key
        String redisKey ="dish_" + dish.getCategoryId()+"_"+dish.getStatus();

        //使用key获取数据
        dishDtoList = (List<DishDto>)redisTemplate.opsForValue().get(redisKey);

        //如果不是空，说明有数据，直接返回
        if (dishDtoList != null){
            return R.success(dishDtoList);
        }

        //生成条件构造器
        LambdaQueryWrapper<Dish> setmealDtoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDtoLambdaQueryWrapper.eq(Dish::getCategoryId, dish.getCategoryId()).eq(dish.getStatus()!= null,Dish::getStatus,dish.getStatus());


        Page<Dish> page = dishService.page(new Page<>(), setmealDtoLambdaQueryWrapper);

        List<Dish> records = page.getRecords();


        dishDtoList =  records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            //数据拷贝
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = dishDto.getCategoryId();

            Category byId = categoryService.getById(categoryId);
            String name1 = byId.getName();
            dishDto.setCategoryName(name1);
            //获取菜品口味
            LambdaQueryWrapper<DishFlavor> DishGetF = new LambdaQueryWrapper<>();
            Long l = item.getId();
            DishGetF.eq(DishFlavor::getDishId,l);

            dishDto.setFlavors(flavorService.list(DishGetF));

            return dishDto;
        }).collect(Collectors.toList());

        //把数据存入rides,设置存活时间60分钟
        redisTemplate.opsForValue().set(redisKey,dishDtoList,60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }
}
