package com.cheng.dao.impl;

import com.cheng.dao.UserDao;
import com.cheng.vo.User;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cheng
 *         2018/11/2 21:49
 */
@Component
public class UserDaoImpl implements UserDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public User getUserByUsername(String username) {

        String sql = "select username,password from users where username =?";

        List<User> list = jdbcTemplate.query(sql, new String[]{username}, (resultSet, i) -> {
            User user = new User();
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            return user;
        });

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public List<String> queryRolesByUsername(String username) {

        String sql = "select role_name from user_roles where username = ?";

        return jdbcTemplate.query(sql, new String[]{username},
                (resultSet, i) -> resultSet.getString("role_name"));
    }
}
