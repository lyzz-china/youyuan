package com.youyuan.controller;

import com.youyuan.common.dto.ResultDTO;
import com.youyuan.common.util.NetUtil;
import com.youyuan.common.util.StringUtil;
import com.youyuan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
*
* @author yizhong.liao
* @createTime 2021/9/1 16:32
*/
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResultDTO loginAuthentication(@RequestBody Map<String, String> loginData , HttpServletRequest request) {
        String loginAccount = loginData.get("loginAccount");
        String password = loginData.get("password");
        String loginIp = NetUtil.getIpAddress(request);

        Map<String, String> authOptions = new HashMap<>();
        authOptions.put("loginIp", loginIp);
        authOptions.put("uaInfo", StringUtil.getStringValue(request.getHeader("USER-AGENT")).toLowerCase());
        authOptions.put("genToken", "Y");

        ResultDTO result = userService.authUserLogin(loginAccount, password, authOptions);
        log.info("user authentication result = " + result.getResultCode());
        return result;
    }
}
