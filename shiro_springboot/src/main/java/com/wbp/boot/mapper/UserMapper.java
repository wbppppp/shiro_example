package com.wbp.boot.mapper;

import com.wbp.boot.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    User findByUsername(@Param("username") String username);

    int addUser(User user);

    void addUserAndRole(User user);
}
