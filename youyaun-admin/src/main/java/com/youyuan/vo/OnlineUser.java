package com.youyuan.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
*
* @author yizhong.liao
* @createTime 2021/9/1 12:02
*/
@Data
public class OnlineUser implements Serializable {

    private String userId;
    private String loginAccount;
    private Integer userStatus;
    private Date lastLoginTime;
    private String lastLoginIp;
    private String userName;
    private String workPhone;
    private String userMail;
    private String userType;


}
