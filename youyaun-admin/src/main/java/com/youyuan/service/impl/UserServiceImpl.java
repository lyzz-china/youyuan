package com.youyuan.service.impl;

import com.youyuan.common.cache.CacheService;
import com.youyuan.common.constants.CacheNames;
import com.youyuan.common.dto.ResultDTO;
import com.youyuan.common.util.DateTimeUtil;
import com.youyuan.common.util.NetUtil;
import com.youyuan.common.util.StringUtil;
import com.youyuan.mapper.UserMapper;
import com.youyuan.model.UserDTO;
import com.youyuan.service.UserService;
import com.abi.eb.common.util.EncryptUtil;
import com.youyuan.vo.OnlineUser;
import com.youyuan.vo.UserToken;
import io.jsonwebtoken.*;
import jdk.nashorn.internal.parser.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
*
* @author yizhong.liao
* @createTime 2021/9/1 12:09
*/
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheService defaultCacheService;

    @Value("${youyuan.token.secretKey}")
    private String tokenSecretKey;
    @Value("${youyuan.token.expireMinutes}")
    private int tokenExpireMinutes;

    private static final String TOKEN_CACHE_KEY_TOKEN_PREFIX = "token_abi_";
    private static final String SU_ACCOUNT = "admin";

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

    @Override
    public ResultDTO authUserLogin(String loginAccount, String password, Map<String, String> authOptions) {
        if (StringUtil.isEmpty(loginAccount) || StringUtil.isEmpty(password)) {
            return ResultDTO.instance("40101", "用户账号或密码为空");
        }

        UserDTO userDTO = queryUserByLoginAccount(loginAccount);
        if (null == userDTO) {
            return ResultDTO.instance("40102", "用户信息不存在");
        }
        if(userDTO.getUserStatus() != 1){
            return ResultDTO.instance("01","用户被禁用或冻结");
        }
        String uaInfo = StringUtil.getStringValue(authOptions.get("uaInfo"));
        String loginIp = StringUtil.getStringValue(authOptions.get("loginIp"));

        String userSecret = userDTO.getUserSecret();
        String encryptedPwd = getEncryptedPwd(password, userSecret);

        userDTO.setLastLoginIp(loginIp);
        userDTO.setLastLoginTime(DateTimeUtil.getCurrentDate());

        if (!userDTO.getPassword().toUpperCase().equals(encryptedPwd)) {
//            saveUserLoginHistory(loginData);
            return ResultDTO.instance("30103", "账号或密码错误");
        }
        ResultDTO result = loginData(userDTO);
//        saveUserLoginHistory(loginData);
        return result;
    }

    @Override
    public ResultDTO loginData(UserDTO userDTO) {
        if (null == userDTO || StringUtil.isEmpty(userDTO.getUserId())) {
            return ResultDTO.instance("31201", "无用户信息");
        }
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setUserId(userDTO.getUserId());
        onlineUser.setWorkPhone(userDTO.getWorkPhone());
        onlineUser.setUserMail(userDTO.getUserMail());
        onlineUser.setLoginAccount(userDTO.getLoginAccount());
        onlineUser.setUserStatus(userDTO.getUserStatus());
        onlineUser.setLastLoginTime(DateTimeUtil.getCurrentDate());
        onlineUser.setLastLoginIp(userDTO.getLastLoginIp());
        onlineUser.setUserName(userDTO.getUserName());
        onlineUser.setUserType(userDTO.getUserType());

        //TODO 权限

        UserToken userToken = createUserToken(onlineUser);
        ResultDTO result = ResultDTO.instance();
        result.addDataEntry("userToken", userToken.getToken());
        result.addDataEntry("tokenExpireTime", userToken.getExpireTime());
        // 将OnlineUser对象加入返回对象中
        result.addDataEntry("userInfo", onlineUser);
        return result;
    }

    @Override
    public UserToken createUserToken(OnlineUser olUser) {
        return createUserToken(olUser, tokenExpireMinutes);
    }

    @Override
    public UserToken createUserToken(OnlineUser olUser, int expireMinutes) {
        UserToken userToken = new UserToken();
        Date issueTime = DateTimeUtil.getCurrentDate();
        Date expireTime = new Date(issueTime.getTime() + expireMinutes * 60 * 10000);
        String token = Jwts.builder().setSubject(olUser.getLoginAccount())
                .claim("userId",olUser.getUserId())
                .setIssuedAt(issueTime)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, tokenSecretKey)
                .compact();
        log.info("generated token = " + token);
        //TODO 保存到缓存
        defaultCacheService.put(CacheNames.USER_TOKEN_CACHE.getName(), TOKEN_CACHE_KEY_TOKEN_PREFIX + token, olUser);
        userToken.setToken(token);
        userToken.setIssueTime(issueTime);
        userToken.setExpireTime(expireTime);
        return userToken;
    }

    @Override
    public OnlineUser getOnlineUserByToken(String token) {
        if (StringUtil.isEmpty(token)) {
            return null;
        }
        return defaultCacheService.get(CacheNames.USER_TOKEN_CACHE.getName(), TOKEN_CACHE_KEY_TOKEN_PREFIX + token,
                OnlineUser.class);
    }

    @Override
    public ResultDTO checkAuthToken(String token) {
        if (StringUtil.isEmpty(token)) {
            return ResultDTO.instance("30321","token为空");
        }
        String authUserId;
        try {
            Claims body = Jwts.parser().setSigningKey(tokenSecretKey).parseClaimsJws(token).getBody();
            authUserId = body.get("userId", String.class);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            return ResultDTO.instance("30202","token异常或已过期");
        }
        OnlineUser onlineUser = getOnlineUserByToken(token);
        if (onlineUser == null || StringUtil.isEmpty(authUserId)) {
            return ResultDTO.instance("30203","token已过期");
        }
        if (!onlineUser.getUserId().equalsIgnoreCase(authUserId)) {
            return ResultDTO.instance("30204","token信息异常");
        }
        return ResultDTO.instance();
    }

    @Override
    public OnlineUser findOnlineUser(HttpServletRequest request) {
        String token = NetUtil.fetchBearerToken(request);
        ResultDTO resultDTO = checkAuthToken(token);
        if (resultDTO.isFailure()) {
            return null;
        }
        return getOnlineUserByToken(token);
    }


}
