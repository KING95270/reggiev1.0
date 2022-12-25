package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.currency.R;
import com.example.reggie.entity.Orders;
import com.example.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    OrdersService ordersService;

    /**
     * 支付功能
     * @return
     */
    @PostMapping("/submit")
    public R<String> zhiFu(@RequestBody Orders orders, HttpSession session){

        String userId = session.getAttribute("userId").toString();
        long l = Long.parseLong(userId);

        ordersService.submit(orders,l);

        return R.success("支付成功");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(Integer page,Integer pageSize){
        Page p = new Page(page,pageSize);

        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.orderByDesc(Orders::getCheckoutTime);

        Page page1 = ordersService.page(p,ordersLambdaQueryWrapper);

        return R.success(page1);
    }


}
