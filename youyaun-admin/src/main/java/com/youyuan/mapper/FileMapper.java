package com.youyuan.mapper;

import com.youyuan.model.FileDTO;
import org.apache.ibatis.annotations.Mapper;


/**
*
* @author yizhong.liao
* @createTime 2021/8/30 17:50
*/
@Mapper
public interface FileMapper {
    /**
     * 保存文件上传记录
     * @param fileDTO
     * @return
     */
    int insertFile(FileDTO fileDTO);
}
