package com.wbp.spring.shiro.realm;

import com.wbp.spring.shiro.service.ShiroService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 *  1.如果只要进行认证的继承AuthenticatingRealm即可
 *      并实现getAuthenticationInfo
 *
 *  2.也可以继承AuthorizingRealm（继承AuthenticatingRealm）,既可以认证，也可以授权
 */
public class ShiroRealm extends AuthorizingRealm {

    /**
     * 注意：
     *      Realm中注入service时，applicaiton.xml如果通过<bean>来创建，则会为null，
     * 要使用<context:component-scan/>来扫描配置service
     *
     */
    @Resource
    public ShiroService shiroService;

    /**
     *  认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("进入realm -->" + token);

        //1.AuthenticationToken转换为UsernamePasswordToken
        UsernamePasswordToken uptoken = (UsernamePasswordToken)token;

        //2.从UsernamePasswordToken获取username
        String username = uptoken.getUsername();

        System.out.println("realm.password -> " + ((UsernamePasswordToken) token).getPassword());
        System.out.println("realm.credentials ->" + token.getCredentials().toString());

        //3.根据username查询数据库获取信息
        shiroService.test();
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
            credentials = "038bdaf98f2037b31f1e75b5b4c9b26e";
        }else if ("user".equals(username)){
            credentials = "098d2c478e9c11555ce2823231e02ec1";
        }

        SimpleAuthenticationInfo info;
        //info = new SimpleAuthenticationInfo(principal,credentials,realmName);
        info = new SimpleAuthenticationInfo(principal,credentials,salt,realmName);
        return info;
    }

    /**
     * 授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1. 从 PrincipalCollection 中来获取登录用户的信息
        Object principal = principals.getPrimaryPrincipal();

        //2. 利用登录的用户的信息来用户当前用户的角色或权限(可能需要查询数据库)
        Set<String> roles = new HashSet<>();
        roles.add("user");
        if("admin".equals(principal)){
            roles.add("admin");
        }

        //3. 创建 SimpleAuthorizationInfo, 并设置其 reles 属性.
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);

        //4. 返回 SimpleAuthorizationInfo 对象.
        return info;
    }
}
