package com.cheng.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义 Realm
 *
 * @author cheng
 *         2018/11/2 20:49
 */
public class CustomRealm extends AuthorizingRealm {

    Map<String, String> userMap = new HashMap<>(16);

    {
        userMap.put("cheng", "66f469382db2328c876b700deb336220");

        super.setName("customRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String username = (String) principals.getPrimaryPrincipal();

        Set<String> roles = getRolesByUserName(username);

        Set<String> permissions = getPermissionsByUsername(username);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        // 1. 从主体传过来的认证信息中，获得用户名
        String username = (String) authenticationToken.getPrincipal();

        // 2. 通过用户名到数据库中获取凭证
        String password = getPasswordByUsername(username);
        if (password == null) {
            return null;
        }

        SimpleAuthenticationInfo authenticationInfo =
                new SimpleAuthenticationInfo(username, password, "customRealm");

        // 设置加密的 盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("cheng"));

        return authenticationInfo;
    }

    private Set<String> getRolesByUserName(String username) {

        Set<String> set = new HashSet<>();

        // 从数据库或者缓存中获取角色数据
        set.add("admin");
        set.add("user");
        return set;
    }

    private Set<String> getPermissionsByUsername(String username) {

        Set<String> set = new HashSet<>();

        set.add("user:delete");
        set.add("user:update");
        return set;
    }

    /**
     * 模拟数据库查询凭证
     *
     * @param username
     * @return
     */
    private String getPasswordByUsername(String username) {
        return userMap.get(username);
    }

    public static void main(String[] args) {

        // 密码 + 盐 加密后的结果
        Md5Hash md5Hash = new Md5Hash("123", "cheng");
        System.out.println(md5Hash.toString());
    }
}
