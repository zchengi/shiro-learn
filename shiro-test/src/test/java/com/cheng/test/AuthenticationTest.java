package com.cheng.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * @author cheng
 *         2018/11/2 20:04
 */
public class AuthenticationTest {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser() {
        simpleAccountRealm.addAccount("cheng", "123", "admin", "user");
    }

    @Test
    public void testAuthentication() {

        // 1. 构建 SecurityManager 环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        // 2. 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("cheng", "123");
        subject.login(token);

        System.out.println("isAuthenticated: " + subject.isAuthenticated());

        // 检查用户是否有 admin 角色
        subject.checkRoles("admin", "user");

        // 退出认证
        subject.logout();

        System.out.println("isAuthenticated: " + subject.isAuthenticated());
    }
}
