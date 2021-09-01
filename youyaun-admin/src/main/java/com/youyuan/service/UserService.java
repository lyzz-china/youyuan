package com.youyuan.service;

import com.youyuan.common.dto.ResultDTO;
import com.youyuan.model.UserDTO;

/**
*
* @author yizhong.liao
* @createTime 2021/9/1 12:08
*/
public interface UserService {

    /**
     * 添加用户
     * @param userDTO
     * @return
     */
    ResultDTO addUser(UserDTO userDTO);

    /**
     * 根据登录名查询
     * @param loginAccount
     * @return
     */
    UserDTO queryUserByLoginAccount(String loginAccount);

    /**
     * 根据用户编码查询
     * @param userCode
     * @return
     */
    UserDTO queryUserByCode(String userCode);

}
