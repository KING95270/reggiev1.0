package com.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie.currency.R;
import com.example.reggie.entity.Category;

import java.util.Calendar;

public interface CategoryService extends IService<Category> {
     public  R<String> delete(Long id);


}
