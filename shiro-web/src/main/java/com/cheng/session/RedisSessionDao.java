package com.cheng.session;

import com.cheng.util.JedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author cheng
 *         2018/11/4 16:45
 */
public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    private JedisUtil jedisUtil;

    private final String SHIRO_SESSION_PREFIX = "cheng-session:";


    @Override
    protected Serializable doCreate(Session session) {

        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        saveSession(session);

        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {

        System.out.println("read session");
        if (sessionId == null) {
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);

        return (Session) SerializationUtils.deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {

        if (session != null && session.getId() != null) {
            saveSession(session);
        }
    }

    @Override
    public void delete(Session session) {

        if (session == null || session.getId() == null) {
            return;
        }

        byte[] key = getKey(session.getId().toString());
        jedisUtil.delete(key);
    }

    @Override
    public Collection<Session> getActiveSessions() {

        Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);
        Set<Session> sessions = new HashSet<>();
        if (CollectionUtils.isEmpty(keys)) {
            return sessions;
        }

        for (byte[] key : keys) {
            Session session = (Session) SerializationUtils.deserialize(jedisUtil.get(key));
            sessions.add(session);
        }

        return sessions;
    }

    private void saveSession(Session session) {

        if (session != null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);

            jedisUtil.set(key, value);
            jedisUtil.expire(key, 600);
        }
    }

    private byte[] getKey(String key) {
        return (SHIRO_SESSION_PREFIX + key).getBytes();
    }
}
