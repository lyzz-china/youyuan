package com.youyuan.service.impl;

import com.youyuan.common.dto.ResultDTO;
import com.youyuan.common.util.DateTimeUtil;
import com.youyuan.common.util.StringUtil;
import com.youyuan.mapper.FileMapper;
import com.youyuan.model.FileDTO;
import com.youyuan.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;

/**
* 文件业务
* @author yizhong.liao
* @createTime 2021/8/30 17:47
*/
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private static final String[] ALLOW_FILE_TYPES = { "jpg", "jpeg", "png","heic","heif","zip","image/png","image/jpg","image/jpeg",
            "application/vnd.ms-excel", "application/octet-stream",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.template",
            "application/msword","application/pdf","application/postscript","application/vnd.ms-works",
            "text/plain","video/mpeg","video/x-msvideo","video/mp4","audio/x-pn-realaudio"," image/heic"};
    private static final String[] ALLOW_FILE_EXT_NAMES = {"jpg", "jpeg", "png", "xls", "xlsx", "zip","rar"};


    @Value("${youyuan.file.repository.path}")
    private String filePath;
    @Value("${youyuan.file.repository.base}")
    private String fileBase;

    @Autowired
    private FileMapper fileMapper;

    @Override
    public ResultDTO uploadFile(MultipartFile file) throws IOException {
        return uploadFile(file, null);
    }

    @Override
    public ResultDTO uploadFile(MultipartFile file, String filePath) throws IOException {
        ResultDTO result = ResultDTO.instance();
        if (null == file) {
            result.setResultCode("10351");
            result.setResultMsg("上传文件为空");
        }
        String uploadContentType = file.getContentType().toLowerCase();
        log.debug("upload file conent type is : " + uploadContentType);
        //检查上传文件是否符合上传文件类型
        boolean bAllowed = false;
        for (String allowFileType : ALLOW_FILE_TYPES) {
            if (uploadContentType.equals(allowFileType)) {
                bAllowed = true;
                break;
            }
        }
        if (!bAllowed) {
            result.setResultCode("10352");
            result.setResultMsg("上传文件不符合规定格式");
            return result;
        }
        String originalFilename = file.getOriginalFilename();
        Long size = file.getSize();
        String extName = "";
        int extPos = originalFilename.lastIndexOf(".");
        if (extPos != -1) {
            extName = originalFilename.substring(extPos + 1);
        }

        //检查文件扩展名
        bAllowed = false;
        for (String allowFileExtName : ALLOW_FILE_EXT_NAMES) {
            if (extName.equalsIgnoreCase(allowFileExtName)) {
                bAllowed = true;
                break;
            }
        }

        if (!bAllowed) {
            result.setResultCode("10352");
            result.setResultMsg("上传文件非指定格式");
            return  result;
        }

        FileDTO fileDTO = new FileDTO();
        String fileId = StringUtil.genUUID();
        String subFolder = StringUtil.hashBy256(fileId);

        if (StringUtil.isEmpty(filePath)) {
            filePath = this.filePath;
        }

        File destFolder = new File(fileBase + File.separator + filePath + File.separator + subFolder);
        if (!destFolder.exists()) {
            destFolder.mkdirs();
        }
        String destFileName = fileId;
        if (!extName.equals("")) {
            destFileName = destFileName + "." + extName;
        }
        File destFile = new File(fileBase + File.separator + filePath + File.separator + subFolder + File.separator + destFileName);
        file.transferTo(destFile);
        fileDTO.setFileId(fileId);
        fileDTO.setMediaType(uploadContentType);
        fileDTO.setFileFormat(extName);
        fileDTO.setFilePath(filePath + File.separator + subFolder + File.separator + destFileName);
        fileDTO.setOriginalName(originalFilename);
        fileDTO.setIsEncrypt(0);
        fileDTO.setIsZip(0);
        fileDTO.setFileSize(size);
        fileDTO.setCreateUserId("SYSTEM");
        fileDTO.setCreateDate(DateTimeUtil.getCurrentDate());
        boolean bInsert = insertFile(fileDTO);
        if (!bInsert) {
            result.setResultCode("10353");
            result.setResultMsg("上传文件失败");
            return result;
        }
        fileDTO.setFileUri(transferPathAsImageUri(fileDTO.getFilePath()));
        result.addDataEntry("fileDTO", fileDTO);
        return result;
    }

    public String transferPathAsImageUri(String fileServerPath) {
        if (StringUtil.isEmpty(fileServerPath)) {
            return null;
        }
        return "/" + fileServerPath.replaceAll("\\\\", "/");
    }

    @Override
    public boolean insertFile(FileDTO fileDTO) {
        if (fileDTO == null) {
            return false;
        }
        int i = fileMapper.insertFile(fileDTO);
        if ( i<= 0) {
            return false;
        }
        return true;
    }

    @Override
    public FileDTO queryFileById(String fileId) {
        if (StringUtil.isEmpty(fileId)) {
            return null;
        }
        return fileMapper.queryFileById(fileId);
    }

    @Override
    public String getRepositoryBase() {
        return this.fileBase;
    }

    @Override
    public String getDefaultFilePath() {
        return this.filePath;
    }

    @Override
    public FileInputStream getFileAsInputStream(String fileId) {
        FileDTO fileDTO = queryFileById(fileId);
        if (null == fileDTO) {
            return null;
        }
        String filePath = fileDTO.getFilePath();
        String fullPath = fileBase + File.separator + filePath;
        File f = new File(fullPath);
        if (!f.exists()) {
            return null;
        }
        try{
            return new FileInputStream(f);
        }catch(IOException e){
            log.debug("fileService.getFileAsInputStream exception occurs. ", e);
        }
        return null;
    }


}
