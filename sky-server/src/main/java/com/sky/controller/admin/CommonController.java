package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    private final AliOssUtil aliOssUtil;

    public CommonController(AliOssUtil aliOssUtil) {
        this.aliOssUtil = aliOssUtil;
    }

    @RequestMapping("/upload")
    @ApiOperation("文件上传")
    public Result upload(MultipartFile file){
        log.info("文件上传:{}",file);
        try {
            //获取原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名中的后缀名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //生成随机文件名
            String fileName = UUID.randomUUID().toString() + extension;
            //上传文件
            String filePath = aliOssUtil.upload(file.getBytes(), fileName);
            return Result.success(filePath);
        } catch (Exception e) {
            log.error("文件上传失败:{}",e);
        }
        return Result.success();
    }
}
