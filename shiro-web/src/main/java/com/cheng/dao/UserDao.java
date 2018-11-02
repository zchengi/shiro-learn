package com.cheng.dao;

import com.cheng.vo.User;

import java.util.List;

/**
 * @author cheng
 *         2018/11/2 21:48
 */
public interface UserDao {

    User getUserByUsername(String username);

    List<String> queryRolesByUsername(String username);
}
