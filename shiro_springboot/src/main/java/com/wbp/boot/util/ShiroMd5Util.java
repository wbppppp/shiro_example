package com.wbp.boot.util;

import com.wbp.boot.entity.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class ShiroMd5Util {

    public static void main(String[] args) {
        User user = new User("admin","123");
        user = ShiroMd5Util.SysMd5(user);
//2bbffae8c52dd2532dfe629cecfb2c85
        System.out.println(user.getPassword());
    }

    //添加user的密码加密方法
    //要和shiro的加密方式保持一致
    public static User  SysMd5(User user) {
        //加密方式
        String hashAlgorithmName = "MD5";
        //盐值
        ByteSource salt = ByteSource.Util.bytes(user.getUsername());
        //加密次数
        int hashIterations = 1024;
        Object result = new SimpleHash(hashAlgorithmName, user.getPassword(), salt, hashIterations);
        user.setPassword(result.toString());
        return user;
    }
}

