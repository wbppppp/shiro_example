package com.wbp.spring.shiro.controller;

import com.wbp.spring.shiro.service.ShiroService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/shiro")
public class ShiroController {

    private static final transient Logger log = LoggerFactory.getLogger(ShiroController.class);

    @Autowired
    public ShiroService shiroService;

    public static void main(String[] args) {
        String hashAlgorithmName = "SHA1";
        ByteSource salt = ByteSource.Util.bytes("admin");
        Object result = new SimpleHash(hashAlgorithmName, "123456", salt, 1024);

        System.out.println(result);
    }

    /**
     * 测试shiro注解
     *
     * @return
     */
    @RequestMapping("/testAnno")
    public String testAnno(){
        shiroService.testMethod();
        return "list";
    }


    @RequestMapping("/login")
    public String login(String username, String password){

        Subject currentUser = SecurityUtils.getSubject();

        // Do some stuff with a Session (no need for a web or EJB container!!!)
        // 测试使用 Session
        // 获取 Session: Subject#getSession()
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        String value = (String) session.getAttribute("someKey");
        if (value.equals("aValue")) {
            log.info("---> Retrieved the correct value! [" + value + "]");
        }

        // let's login the current user so we can check against roles and permissions:
        // 测试当前的用户是否已经被认证. 即是否已经登录.
        // 调动 Subject 的 isAuthenticated()
        if (!currentUser.isAuthenticated()) {
            // 把用户名和密码封装为 UsernamePasswordToken 对象
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            // rememberme
            token.setRememberMe(true);
            try {
                // 执行登录.
                currentUser.login(token);
            }
            // 若没有指定的账户, 则 shiro 将会抛出 UnknownAccountException 异常.
            /*catch (UnknownAccountException uae) {
                log.info("----> There is no user with username of " + token.getPrincipal());
                return "login";
            }
            // 若账户存在, 但密码不匹配, 则 shiro 会抛出 IncorrectCredentialsException 异常。
            catch (IncorrectCredentialsException ice) {
                log.info("----> Password for account " + token.getPrincipal() + " was incorrect!");
                return "login";
            }
            // 用户被锁定的异常 LockedAccountException
            catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            }*/
            // ... catch more exceptions here (maybe custom ones specific to your application?
            // 所有认证时异常的父类.
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
                System.out.println("登录失败：" + ae.getMessage());
                return "redirect:/login.jsp";
            }
        }

        return "redirect:/list.jsp";
    }
}
