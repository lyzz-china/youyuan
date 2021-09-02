package com.youyuan;

import com.youyuan.common.cache.CacheService;
import com.youyuan.common.constants.CacheNames;
import com.youyuan.mapper.FileMapper;
import com.youyuan.mapper.UserMapper;
import com.youyuan.model.FileDTO;
import com.youyuan.model.UserDTO;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.helpers.FieldMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;

import java.util.List;

@SpringBootTest
class YouyaunAdminApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private CacheService defaultCacheService;

    @Test
    void contextLoads() {
        UserDTO userDTO = userMapper.queryUserByCode("1");
        System.out.println(userDTO);
//
//        FileDTO fileDTO = fileMapper.queryFileById("573ca1aa-b3e3-4e1b-b657-fcc9e80b20d4");
//        System.out.println(fileDTO);
    }

    @Test
    public void test(){
        UserDTO lyz = userMapper.queryUserByLoginAccount("lyz");
        UserDTO admin = userMapper.queryUserByLoginAccount("admin");
//        System.out.println(userDTO);
        defaultCacheService.put(CacheNames.USER_CACHE.getName(),lyz.getUserCode(),lyz);
//        defaultCacheService.put(CacheNames.USER_CACHE.getName(),admin.getUserCode(),admin);

//        List<UserDTO> u00002 = defaultCacheService.getList(CacheNames.USER_CACHE.getName(), "U00002", UserDTO.class);

//        defaultCacheService.evict(CacheNames.USER_CACHE.getName(), lyz);

        defaultCacheService.clear(CacheNames.USER_CACHE.name());

        UserDTO userCache1 = defaultCacheService.get("userCache", "U00002", UserDTO.class);
        UserDTO userCache2 = defaultCacheService.get("userCache", "U00001", UserDTO.class);
        System.out.println(userCache1);
        System.out.println(userCache2);
    }

}
