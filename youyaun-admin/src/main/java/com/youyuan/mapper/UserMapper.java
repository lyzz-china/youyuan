package com.youyuan.mapper;

import com.youyuan.model.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
*
* @author yizhong.liao
* @createTime 2021/9/1 12:11
*/
@Repository
@Mapper
public interface UserMapper {

    /**
     * 保存用户信息
     * @param userDTO
     * @return
     */
    int insertUser(UserDTO userDTO);

    /**
     * 根据登录名查询用户信息
     * @param loginAccount
     * @return
     */
    UserDTO queryUserByLoginAccount(String loginAccount);

    /**
     * 根据用户编码查询用户信息
     * @param userCode
     * @return
     */
    UserDTO queryUserByCode(String userCode);
}
