package com.example.reggie.Exception;

import com.example.reggie.currency.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class AllException {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> Ex(SQLIntegrityConstraintViolationException exception) {

        //定义全局异常

      log.error("失败！！！！！{}",exception.getMessage());

      //判断错误信息是否包含Duplicate entry 该错误为账户已存在
      if (exception.getMessage().contains("Duplicate entry")){
          List<String> s = Arrays.asList(exception.getMessage().split(" "));
          return R.error(s.get(2) + "已存在");
      }

        return R.error("未知错误");


    }
}
