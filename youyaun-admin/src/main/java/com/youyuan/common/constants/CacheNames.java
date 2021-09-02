package com.youyuan.common.constants;

/**
*
* @author yizhong.liao
* @createTime 2021/9/2 12:00
*/
public enum CacheNames {
    USER_TOKEN_CACHE("userTokenCache"),
    USER_CACHE("userCache"),
    FILE_CACHE("fileCache");

    private String name;

    CacheNames(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }
}
