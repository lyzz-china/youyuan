package com.youyuan.service;

import com.youyuan.common.dto.ResultDTO;
import com.youyuan.model.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* 文件上传业务
* @author yizhong.liao
* @createTime 2021/8/30 17:14
*/
public interface FileService {

    /**
     * 上传文件
     * @param file
     * @return
     */
    ResultDTO uploadFile(MultipartFile file) throws IOException;

    /**
     * 上传文件
     * @param file
     * @param filePath
     * @return
     */
    ResultDTO uploadFile(MultipartFile file, String filePath) throws IOException;

    boolean insertFile(FileDTO fileDTO);
}
