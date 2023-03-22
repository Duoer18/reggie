package com.duoer.takeout.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/common")
@Slf4j
public class UserCommonController {
    @Value("${reggie.upload-path}")
    private String uploadPath;

    /**
     * 文件下载接口
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        log.info("download file {}", name);

        try (FileInputStream inputStream = new FileInputStream(uploadPath + name)) {
            response.setContentType("file/all");
            ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
        }
    }
}
