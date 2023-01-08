package com.example.reggie.commont;

import com.example.reggie.currency.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.CoyoteOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;


/**
 * 图片上传与下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    //
    @Value("${reggie.path}")
    private String Path;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {

        //原始文件名称
        String originalFilename = file.getOriginalFilename();

        //从小数点开始分割出后面的所有字符串
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        //为了防止重名覆盖等问题出现，使用uuid重新定义名称
        String s = UUID.randomUUID().toString() + substring;

        //转存至指定位置
        file.transferTo(new File(Path + s));

        return R.success(s);
    }

    @GetMapping("/download")
    public void dowload(String name, HttpServletResponse response) throws Exception {
        //以这种格式写入浏览器
        response.setContentType("image/jpeg");

        //输入流,通过流读取内容
        FileInputStream fileInputStream = new FileInputStream(new File(Path + name));

        //输出流，通过输出流将文件传回浏览器
        ServletOutputStream outputStream = response.getOutputStream();

        byte[] bytes = new byte[1024];

        int len = 0;

        while ((len = fileInputStream.read(bytes)) != -1) {
            //写入浏览器
            outputStream.write(bytes, 0, len);
            //刷新一下
            outputStream.flush();
        }
        fileInputStream.close();
        outputStream.close();
    }

}
