package com.duoer.reggie.controller.impl;

import com.duoer.reggie.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.upload-path}")
    private String uploadPath;

    /**
     * 文件上传接口
     * 可使用 @RequestPart 自定义参数名
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        log.info("upload file {}, saved in {}", file.getOriginalFilename(), uploadPath);

        // 获取原文件名，并生成保存的文件名
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String filename = UUID.randomUUID() + suffix;

        // 若保存文件的目录不存在，则先创建该目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists() && !uploadDir.mkdirs()) { // 创建目录失败
            return Result.failed("上传文件失败");
        }

        // 保存文件到本地
        file.transferTo(new File(uploadPath + filename));
        return Result.success(filename);
    }

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
