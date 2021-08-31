package com.youyuan.service;

import com.youyuan.common.dto.ResultDTO;
import com.youyuan.model.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
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

    /**
     * 保存一条文件信息
     * @param fileDTO
     * @return
     */
    boolean insertFile(FileDTO fileDTO);

    /**
     * 根据文件id查询文件信息
     * @param fileId
     * @return
     */
    FileDTO queryFileById(String fileId);

    /**
     * 获取文件基础路径
     * @return
     */
    String getRepositoryBase();

    /**
     * 获取默认文件路径
     * @return
     */
    String getDefaultFilePath();

    /**
     *
     * @param fileId
     * @return
     */
    FileInputStream getFileAsInputStream(String fileId);
}
