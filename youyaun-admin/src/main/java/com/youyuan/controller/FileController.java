package com.youyuan.controller;

import com.youyuan.common.dto.ResultDTO;
import com.youyuan.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
*
* @author yizhong.liao
* @createTime 2021/8/30 17:11
*/
@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResultDTO upload(@RequestParam("file") MultipartFile file){
        try {
            return fileService.uploadFile(file);
        } catch (IOException e) {
            log.error("file upload exception occurred. ", e);
            return ResultDTO.instance("41021","文件上传失败");
        }
    }
}
