package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie.currency.R;
import com.example.reggie.entity.Category;
import com.example.reggie.entity.Dish;
import com.example.reggie.entity.ShoppingCart;
import com.example.reggie.service.CategoryService;
import com.example.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.util.resources.cldr.mg.LocaleNames_mg;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> listR() {
        List<ShoppingCart> list = shoppingCartService.list();
        return R.success(list);
    }

    //加入购物车
    @PostMapping("/add")
    public R<String> save(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
        String userId = session.getAttribute("userId").toString();
        long l = Long.parseLong(userId);
        shoppingCart.setUserId(l);
        shoppingCartService.save(shoppingCart);
        return R.success("");
    }

    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());

        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);

        return R.success("");
    }
}
