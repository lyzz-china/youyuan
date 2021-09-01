package com.youyuan.controller;

import com.youyuan.common.dto.ResultDTO;
import com.youyuan.common.util.DateTimeUtil;
import com.youyuan.model.UserDTO;
import com.youyuan.service.UserService;
import com.youyuan.vo.OnlineUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
*
* @author yizhong.liao
* @createTime 2021/9/1 12:00
*/
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResultDTO addUser(HttpServletRequest request, @RequestBody UserDTO userDTO) {
//        OnlineUser onlineUser = getCurrentUser(request);
//        userDTO.setCreateUserId(onlineUser.getUserId());
//        userDTO.setCreateDate(DateTimeUtil.getCurrentDate());
        return userService.addUser(userDTO);
    }

}
