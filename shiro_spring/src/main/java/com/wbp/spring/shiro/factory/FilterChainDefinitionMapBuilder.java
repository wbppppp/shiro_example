package com.wbp.spring.shiro.factory;

import java.util.LinkedHashMap;

public class FilterChainDefinitionMapBuilder {

    public LinkedHashMap<String,String> builder(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();

        //这里可添加数据库查询操作，来进行权限限制
        map.put("/login","anon");
        map.put("/shiro/login","anon");
        map.put("/shiro/logout","logout");
        map.put("/user.jsp","authc,roles[user]");
        map.put("/admin.jsp","authc,roles[admin]");
        map.put("/list.jsp","user"); //认证或者记住我 都可以访问

        map.put("/**","authc"); //认证之后可以访问

        return map;
    }
}
