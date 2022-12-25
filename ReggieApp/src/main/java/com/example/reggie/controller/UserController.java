package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie.currency.R;
import com.example.reggie.entity.User;
import com.example.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j

public class UserController {

    @Autowired
    UserService userService;

    //登录
    @PostMapping("/login")
    public R<String> login(@RequestBody Map map, HttpSession session){
        String phone = map.get("phone").toString();
        User user = new User();
        user.setPhone(phone);

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone,phone);


        if (userService.getOne(userLambdaQueryWrapper) == null){
            userService.save(user);
        }
        User one = userService.getOne(userLambdaQueryWrapper);
        session.setAttribute("userId",one.getId());
        session.setAttribute("user",map.get("phone"));
        session.setMaxInactiveInterval(-1);

        return R.success("成功");
    }

    //退出
    @PostMapping("/loginout")
    public R<String> LoginOut(HttpServletRequest request){
        request.removeAttribute("user");
        return R.success("退出成功");
    }

}
