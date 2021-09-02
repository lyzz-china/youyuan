package com.youyuan.service;

import com.youyuan.common.dto.ResultDTO;
import com.youyuan.model.UserDTO;
import com.youyuan.vo.OnlineUser;
import com.youyuan.vo.UserToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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

    /**
     * 授权登录
     * @param loginAccount
     * @param password
     * @param authOptions
     * @return
     */
    ResultDTO authUserLogin(String loginAccount, String password, Map<String, String> authOptions);

    /**
     * 登录后用户数据
     * @param userDTO
     * @return
     */
    ResultDTO loginData(UserDTO userDTO);

    /**
     * 生成Token
     * @param olUser
     * @return
     */
    UserToken createUserToken(OnlineUser olUser);

    /**
     * 生成Token
     * @param olUser
     * @param expireMinutes
     * @return
     */
    UserToken createUserToken(OnlineUser olUser, int expireMinutes);

    /**
     * 根据token获取在线用户
     * @param token
     * @return
     */
    OnlineUser getOnlineUserByToken(String token);

    /**
     * 检查token
     * @param token
     * @return
     */
    ResultDTO checkAuthToken(String token);

    /**
     * 查找在线用户
     * @param request
     * @return
     */
    OnlineUser findOnlineUser(HttpServletRequest request);


}
