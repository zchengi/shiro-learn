package com.cheng.test;

import com.cheng.shiro.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * 自定义 Realm
 *
 * @author cheng
 *         2018/11/2 20:54
 */
public class CustomerRealmTest {
    @Test
    public void testAuthentication() {

        CustomRealm customRealm = new CustomRealm();
        // 1. 构建 SecurityManager 环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        // 2. 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("cheng", "123");
        subject.login(token);

        System.out.println("isAuthenticated: " + subject.isAuthenticated());

        subject.checkRole("admin");
//
        subject.checkPermission("user:delete");
        subject.checkPermission("user:update");
    }
}
