package com.wbp.boot.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

/**
 *  1.如果只要进行认证的继承AuthenticatingRealm即可
 *      并实现getAuthenticationInfo
 */
public class SecondRealm extends AuthenticatingRealm {


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("SecondRealm -->" + token);

        //1.AuthenticationToken转换为UsernamePasswordToken
        UsernamePasswordToken uptoken = (UsernamePasswordToken)token;

        //2.从UsernamePasswordToken获取username
        String username = uptoken.getUsername();

        System.out.println("realm.password -> " + ((UsernamePasswordToken) token).getPassword());
        System.out.println("realm.credentials ->" + token.getCredentials().toString());

        //3.根据username查询数据库获取信息
        System.out.println("从数据库中获取" + username + "用户信息");

        //4.用户不存在则UnknownAccountException/密码错误抛出IncorrectCredentialsException
        if ("unknown".equals(username)){
            throw new UnknownAccountException("用户不存在");
        }

        //5.根据用户信息判断是否抛出其他异常，比如用户锁定，密码错误
        if ("monster".equals(username)){
            throw new LockedAccountException("用户被锁定");
        }
        //6.根据用户的情况，构建AuthenticationInfo对象返回
        //1). principal: 认证的实体信息. 可以是 username, 也可以是数据表对应的用户的实体类对象.
        Object principal = username;
        //2). credentials: 密码.
        Object credentials = null; //"fc1709d0a95a6be30bc5926fdb7f22f4";
        //3). realmName: 当前 realm 对象的 name. 调用父类的 getName() 方法即可
        String realmName = getName();
        //4). 盐值
        ByteSource salt = ByteSource.Util.bytes(username);

        if ("admin".equals(username)){
            credentials = "ce2f6417c7e1d32c1d81a797ee0b499f87c5de06";
        }else if ("user".equals(username)){
            credentials = "073d4c3ae812935f23cb3f2a71943f49e082a718";
        }

        SimpleAuthenticationInfo info;
        //info = new SimpleAuthenticationInfo(principal,credentials,realmName);
        info = new SimpleAuthenticationInfo(principal,credentials,salt,realmName);
        return info;
    }
}
