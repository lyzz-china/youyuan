package com.youyuan.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
*
* @author yizhong.liao
* @createTime 2021/9/1 11:56
*/
@Getter
@Setter
public class UserDTO {

    private String userId;
    private String userCode;
    private String userName;
    private String userType;
    private String loginAccount;
    private String password;
    private String userSecret;
    private String userMail;
    private String workPhone;
    private Integer userStatus;
    private Integer activeStatus;
    private String lastLoginIp;
    private Date lastLoginTime;
    private String createUserId;
    private Date createDate;
    private String updateUserId;
    private Date updateDate;
    private Date pwdExpireTime;

    private String userTypeName;

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId='" + userId + '\'' +
                ", userCode='" + userCode + '\'' +
                ", userName='" + userName + '\'' +
                ", userType='" + userType + '\'' +
                ", loginAccount='" + loginAccount + '\'' +
                ", password='" + password + '\'' +
                ", userSecret='" + userSecret + '\'' +
                ", userMail='" + userMail + '\'' +
                ", workPhone='" + workPhone + '\'' +
                ", userStatus=" + userStatus +
                ", activeStatus=" + activeStatus +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", createUserId='" + createUserId + '\'' +
                ", createDate=" + createDate +
                ", updateUserId='" + updateUserId + '\'' +
                ", updateDate=" + updateDate +
                ", pwdExpireTime=" + pwdExpireTime +
                ", userTypeName='" + userTypeName + '\'' +
                '}';
    }
}
