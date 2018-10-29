package com.wbp.boot.service;


import com.wbp.boot.entity.User;

public interface UserService {

    User findByUsername(String username);

    boolean addUser(User user);
}
