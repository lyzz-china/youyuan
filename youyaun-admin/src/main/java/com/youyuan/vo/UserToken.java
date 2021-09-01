package com.youyuan.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
*
* @author yizhong.liao
* @createTime 2021/9/1 17:05
*/
@Setter
@Getter
public class UserToken implements Serializable {

    private String userId;
    private String loginId;
    private Date issueTime;
    private Date expireTime;
    private String token;

}
