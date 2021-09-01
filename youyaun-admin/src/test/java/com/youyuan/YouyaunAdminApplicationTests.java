package com.youyuan;

import com.youyuan.mapper.FileMapper;
import com.youyuan.mapper.UserMapper;
import com.youyuan.model.FileDTO;
import com.youyuan.model.UserDTO;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.helpers.FieldMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YouyaunAdminApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileMapper fileMapper;

    @Test
    void contextLoads() {
        UserDTO userDTO = userMapper.queryUserByCode("1");
        System.out.println(userDTO);
//
//        FileDTO fileDTO = fileMapper.queryFileById("573ca1aa-b3e3-4e1b-b657-fcc9e80b20d4");
//        System.out.println(fileDTO);
    }

}
