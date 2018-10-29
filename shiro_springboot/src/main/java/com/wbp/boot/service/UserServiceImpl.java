package com.wbp.boot.service;

import com.wbp.boot.entity.User;
import com.wbp.boot.mapper.UserMapper;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    /**
     * @RequiresAuthentication：表示当前Subject已经通过login
     *              进行了身份验证；即 Subject. isAuthenticated() 返回 true
     * @RequiresUser：表示当前 Subject 已经身份验证或者通过记住我登录的。
     * @RequiresGuest：表示当前Subject没有身份验证或通过记住我登录过，即是游客身份。
     *
     * @RequiresRoles(value={“admin”, “user”}, logical=Logical.AND)：表示当前 Subject 需要角色 admin 和user
     *
     * @RequiresPermissions (value={“ user:a”, “ user:b”},logical= Logical.OR)：表示当前 Subject 需要权限 user:a 或 user:b。
     *
     */
    @RequiresRoles(value = {"admin","user"},logical = Logical.OR)
    public void testMethod(){
        System.out.println("method success:" + new Date());
    }

    @Resource
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public boolean addUser(User user) {
        int i = userMapper.addUser(user);
        System.out.println(user.getUid() + "-------");
        //添加customer角色
        userMapper.addUserAndRole(user);
        return i == 1;
    }
}
