package com.youyuan.controller;

import com.youyuan.common.dto.ResultDTO;
import com.youyuan.common.util.StringUtil;
import com.youyuan.model.FileDTO;
import com.youyuan.service.FileService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

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

    @PostMapping("/delete")
    public ResultDTO deleteFile(@RequestBody Map<String, String> fileData) {
        if (null == fileData) {
            return ResultDTO.instance("01","文件id为空");
        }
        String fileId = fileData.get("fileId");
        boolean deleted = fileService.deleteFile(fileId);
        if(deleted){
            return ResultDTO.instance();
        }else{
            return ResultDTO.instance("41029","文件删除失败");
        }
    }


    @GetMapping("/open")
    public void openFile(HttpServletResponse response,
                              @RequestParam(value = "fileId", required = false, defaultValue = "") String fileId) {
        try {
            if(StringUtil.isEmpty(fileId)){
                response.sendError(HttpStatus.NOT_FOUND.value());
                return ;
            }
            FileDTO fileDTO = fileService.queryFileById(fileId);
            if (null == fileDTO) {
                response.sendError(HttpStatus.NOT_FOUND.value());
                return ;
            }
            String filePath = fileDTO.getFilePath();
            String fullPath = fileService.getRepositoryBase() + File.separator;
            File file = new File(fullPath);
            if (!file.exists()) {
                response.sendError(HttpStatus.NOT_FOUND.value());
                return;
            }
            String fileName = URLEncoder.encode(fileDTO.getOriginalName(), "UTF-8");
            response.setHeader("content-disposition","attachment; filename = " + fileName);
            ServletOutputStream out = response.getOutputStream();
            FileInputStream fis = fileService.getFileAsInputStream(fileId);
            if (fis == null) {
                response.sendError(HttpStatus.NOT_FOUND.value());
                return;
            }
            int ch;
            while ((ch = fis.read()) != -1) {
                out.write(ch);
            }
            fis.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("file open exception occurred. ", e);
        }

    }
}
