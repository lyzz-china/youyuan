package com.youyuan.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
*
* @author yizhong.liao
* @createTime 2021/8/30 17:51
*/
@Setter
@Getter
public class FileDTO implements Serializable {

    private String fileId;
    private String mediaType;
    private String fileFormat;
    private String filePath;
    private Integer isZip;
    private Integer isEncrypt;
    private String originalName;
    private Long fileSize;
    private Integer imageWidth;
    private Integer imageHeight;
    private String createUserId;
    private Date createDate;
    private String updateUserId;
    private Date updateDate;

    private String fileUri;

    private String photoId;

    @Override
    public String toString() {
        return "FileDTO{" +
                "fileId='" + fileId + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", fileFormat='" + fileFormat + '\'' +
                ", filePath='" + filePath + '\'' +
                ", isZip=" + isZip +
                ", isEncrypt=" + isEncrypt +
                ", originalName='" + originalName + '\'' +
                ", fileSize=" + fileSize +
                ", imageWidth=" + imageWidth +
                ", imageHeight=" + imageHeight +
                ", createUserId='" + createUserId + '\'' +
                ", createDate=" + createDate +
                ", updateUserId='" + updateUserId + '\'' +
                ", updateDate=" + updateDate +
                ", fileUri='" + fileUri + '\'' +
                ", photoId='" + photoId + '\'' +
                '}';
    }
}
