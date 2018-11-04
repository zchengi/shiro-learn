package com.cheng.controller;

import com.cheng.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cheng
 *         2018/11/2 21:31
 */
@Controller
public class UserController {

    @RequestMapping(value = "/subLogin", method = RequestMethod.POST,
            produces = "application/json;charset=utf-8")
    @ResponseBody
    public String subLogin(User user) {

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());

        try {
            // 设置 shiro 记住我功能
            token.setRememberMe(user.getRememberMe());
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        if (subject.hasRole("admin")) {
            return "有 admin 权限";
        }

        return "无 admin 权限";
    }

//    @RequiresRoles("admin")
    @RequestMapping(value = "/testRole", method = RequestMethod.GET)
    @ResponseBody
    public String testRole() {
        return "testRole success";
    }

//    @RequiresRoles("admin1")
//    @RequiresPermissions("admin1")
    @RequestMapping(value = "/testRole1", method = RequestMethod.GET)
    @ResponseBody
    public String testRole1() {
        return "testRole1 success";
    }

    @RequestMapping(value = "/testPerms", method = RequestMethod.GET)
    @ResponseBody
    public String testPerms() {
        return "testPerms success";
    }

    @RequestMapping(value = "/testPerms1", method = RequestMethod.GET)
    @ResponseBody
    public String testPerms1() {
        return "testPerms1 success";
    }
}
