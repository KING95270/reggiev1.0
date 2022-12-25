package com.example.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.currency.R;
import com.example.reggie.entity.Employee;
import com.example.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/employee")
public class EmployeeController implements HandlerInterceptor {

    @Autowired
    EmployeeService service;

    /**
     * 登录页面
     *
     * @param request
     * @param response
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, HttpServletResponse response, @RequestBody Employee employee) {
        //1.对获取到的密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名进行数据库查找
        LambdaQueryWrapper<Employee> lambd = new LambdaQueryWrapper<>();
        lambd.eq(Employee::getUsername, employee.getUsername());
        Employee one = service.getOne(lambd);

        //3.对密码进行校验
        if (one == null) {
            return R.error("登陆失败");
        } else {
            if (!password.equals(one.getPassword())) {
                return R.error("密码错误");
            } else if (one.getStatus() == 0) {
                return R.error("账号已禁用");
            } else {
                request.getSession().setAttribute("Employee", one.getId());
                return R.success(one);
            }

        }

    }


    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.removeAttribute("Employee");
        return R.success("成功");
    }

    /**
     * 员工分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public R<Page> page(int page, int pageSize, String name) {

        //构造分页查询插件
        Page page1 = new Page(page, pageSize);

        //创建条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper();
        //添加过滤条件
        wrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加条件排序
//        wrapper.orderByDesc(Employee::getUpdateTime);

        service.page(page1, wrapper);

        return R.success(page1);
    }

    /**
     * 单条查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> select(@PathVariable int id) {
        long ids = Thread.currentThread().getId();
        Employee byId = service.getById(id);
        return R.success(byId);
    }

    /**
     * 新增员工
     *
     * @param employee
     * @param request
     * @return
     */
    @PostMapping()
    public R<String> aa(@RequestBody Employee employee, HttpServletRequest request) {
        //创建初始化密码，对密码进行md5加密后存入数据库
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        service.save(employee);
        return R.success("成功");
    }

    /**
     * 对帐号进行处理
     *
     * @return
     */
    @PutMapping()
    public R<String> update(@RequestBody Employee employee, HttpServletRequest request) {
        long id = Thread.currentThread().getId();
        service.updateById(employee);

        return R.success("操作成功");
    }


}
