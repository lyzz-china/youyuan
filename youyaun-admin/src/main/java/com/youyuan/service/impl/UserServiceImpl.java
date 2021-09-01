package com.youyuan.service.impl;

import com.youyuan.common.dto.ResultDTO;
import com.youyuan.common.util.DateTimeUtil;
import com.youyuan.common.util.StringUtil;
import com.youyuan.mapper.UserMapper;
import com.youyuan.model.UserDTO;
import com.youyuan.service.UserService;
import com.abi.eb.common.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
*
* @author yizhong.liao
* @createTime 2021/9/1 12:09
*/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private String getEncryptedPwd(String password,String secret) {
        return EncryptUtil.PBKDF2Encrypt(password,secret).toUpperCase();
    }

    @Override
    public ResultDTO addUser(UserDTO userDTO) {
        if (null == userDTO) {
            return ResultDTO.instance("42131", "用户信息为空");
        }
        String loginAccount = userDTO.getLoginAccount();
        if (StringUtil.isEmpty(loginAccount)) {
            return ResultDTO.instance("42131", "用户登陆账号为空");
        }
        UserDTO userinfo = queryUserByLoginAccount(loginAccount);
        if (userinfo != null) {
            return ResultDTO.instance("01","该登录名已占用");
        }
        if (StringUtil.isEmpty(userDTO.getUserCode())) {
            return ResultDTO.instance("01","编码为空");
        }
        userinfo = queryUserByCode(userDTO.getUserCode());
        if (userinfo != null) {
            return ResultDTO.instance("01","用户编号已存在");
        }
        String userSecret = StringUtil.genRandomCode(16, StringUtil.RANDOM_CODE_ALPHANUMERIC);
        userDTO.setUserSecret(userSecret);
        String password = userDTO.getPassword();
        ResultDTO result = checkPasswordRegular(userDTO, password);
        if (result.isFailure()) {
            return result;
        }
        String encryptedPwd = getEncryptedPwd(password, userSecret);
        userDTO.setPassword(encryptedPwd);
        if (null == userDTO.getUserStatus()) {
            userDTO.setUserStatus(1);
        }
        userDTO.setActiveStatus(1);
        // 180天过期
        userDTO.setPwdExpireTime(DateTimeUtil.getDateByDayOffset(DateTimeUtil.getCurrentDate(),180));
        int rows = userMapper.insertUser(userDTO);
        if (rows <= 0) {
            return ResultDTO.instance("42199", "添加失败");
        }
        return result;
    }

    private ResultDTO checkPasswordRegular(UserDTO userDTO, String originalNewPwd) {
        if (StringUtil.isEmpty(originalNewPwd)) {
            return ResultDTO.instance("01", "新密码不能为空");
        }

        if (!StringUtil.isEmpty(userDTO.getLoginAccount())) {
            if (originalNewPwd.contains(userDTO.getLoginAccount())) {
                return ResultDTO.instance("01", "新密码不能包含账号信息");
            }
        }
        // 密码必须包含大小写字母数字特殊字符最低15位
        String patten = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{6,}$";
        boolean checkPassword = originalNewPwd.matches(patten);
        if (!checkPassword) {
            return ResultDTO.instance("01", "密码要包含大小写字母、数字、特殊符号且不少于6位");
        }
        return ResultDTO.instance();
    }

    @Override
    public UserDTO queryUserByLoginAccount(String loginAccount) {
        if (StringUtil.isEmpty(loginAccount)) {
            return null;
        }
        return userMapper.queryUserByLoginAccount(loginAccount);
    }

    @Override
    public UserDTO queryUserByCode(String userCode) {
        if (StringUtil.isEmpty(userCode)) {
            return null;
        }
        return userMapper.queryUserByCode(userCode);
    }
}
