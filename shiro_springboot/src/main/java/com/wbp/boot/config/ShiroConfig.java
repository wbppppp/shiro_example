package com.wbp.boot.config;

import com.wbp.boot.realm.ShiroRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /**
     * 这里要返回DefaultWebSecurityManager，不能是SecurityManager
     *
     * @return
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("authRealm") ShiroRealm authRealm){
        System.out.println("security manager");
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        securityManager.setRealm(authRealm);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器.
        //这里使用的是LinkedHashMap，配置的拦截器从上往下执行，优先匹配成功的生效
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        // 配置不会被拦截的链接 顺序判断
        //配置匿名访问路径
        filterChainDefinitionMap.put("/shiro/login", "anon");
        filterChainDefinitionMap.put("/shiro/userLogin","anon");
        filterChainDefinitionMap.put("/shiro/register", "anon");

        filterChainDefinitionMap.put("/druid/*","anon");

        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        /*filterChainDefinitionMap.put("/shiro/logout","logout");*/

        //配置认证通过以后才能访问
        filterChainDefinitionMap.put("/shiro/index", "authc");

        //配置/shiro/admin请求必须有admin角色，多角色配置roles[角色1,角色2]
        filterChainDefinitionMap.put("/shiro/admin","roles[admin]");

        //配置
        filterChainDefinitionMap.put("/shiro/edit","perms[edit]");

        //登录过（通过login或者remember进行登录的）即可访问
        filterChainDefinitionMap.put("/**", "user");
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.ftl"页面
        shiroFilterFactoryBean.setLoginUrl("/shiro/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/shiro/index");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/shiro/unauthorized");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean("credentialMatcher")
    public SimpleCredentialsMatcher credentialMatcher() {
        HashedCredentialsMatcher credentialMatcher = new HashedCredentialsMatcher("MD5");
        credentialMatcher.setHashIterations(1024);
        return credentialMatcher;
    }

    @Bean("authRealm")
    public ShiroRealm myShiroRealm(@Qualifier("credentialMatcher") SimpleCredentialsMatcher credentialMatcher){

        System.out.println("realm");

        ShiroRealm myShiroRealm = new ShiroRealm();

        //缓存管理（默认不启用）
        //（1）ShiroFilterFactoryBean中配置filterChainDefinitionMap.put("/shiro/admin","roles[admin]");
        //（2）index.ftl中
        //      <@shiro.hasRole name="admin">
        //        <a href="/shiro/admin">Admin Page</a>
        //      </@shiro.hasRole>
        //（3）配置缓存管理以后，每次点击Admin Page不需要在进行验证
        //myShiroRealm.setCacheManager(new MemoryConstrainedCacheManager());

        //对密码进行MD5加密
       /* HashedCredentialsMatcher credentialMatcher = new HashedCredentialsMatcher("MD5");
        credentialsMatcher.setHashIterations(1024);*/
       // credentialsMatcher.setStoredCredentialsHexEncoded(false);
        myShiroRealm.setCredentialsMatcher(credentialMatcher);
        return myShiroRealm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

}
