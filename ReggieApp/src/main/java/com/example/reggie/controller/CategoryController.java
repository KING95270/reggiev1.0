package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.currency.R;
import com.example.reggie.entity.Category;
import com.example.reggie.entity.Employee;
import com.example.reggie.mapper.CategoryMapper;
import com.example.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> select(int page, int pageSize) {

        //创建分页构造器
        Page page1 = new Page(page, pageSize);

        //创建条件构造器
        Page page2 = categoryService.page(page1);

        return R.success(page2);
    }


    @DeleteMapping
    public R<String> delete(Long ids) {
        R<String> delete = categoryService.delete(ids);
        return delete;
    }

    //添加
    @PostMapping
    public R<String> insert(@RequestBody Category category) {
        boolean save = categoryService.save(category);

        if (save == true) {
            return R.success("操作成功");
        } else {
            return R.error("操作失败");
        }
    }

    //根据id修改
    @PutMapping
    public R<String> update(@RequestBody Category category) {

        boolean b = categoryService.updateById(category);
        if (b == true) {
            return R.success("操作成功");
        } else {
            return R.error("操作失败");
        }
    }

    //查询菜品分类
    @GetMapping("/list")
    public R<List> list(Category category){
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        List<Category> list = categoryService.list(lambdaQueryWrapper);



        return R.success(list);
    }


}
