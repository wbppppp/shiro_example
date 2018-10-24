package com.wbp.spring.shiro.service;



import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ShiroService {

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

    public void test(){
        System.out.println("数据库操作");
    }
}
