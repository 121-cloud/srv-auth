package otocloud.auth.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.vertx.ext.web.Session;
import otocloud.auth.dao.SessionDAO;

import java.util.HashMap;

/**
 * Created by better/zhangye on 15/9/30.
 */
@Singleton
public class SessionServiceImpl implements SessionService {
    //TODO delete this, 临时存放在内存中，后续应持久化。
    /**
     * (token, session)
     */
    private HashMap<String, Session> sessions = new HashMap<>();

    @Inject
    private SessionDAO sessionDAO;

    public SessionServiceImpl() {

    }


    @Override
    public Session createSession() {
        return sessionDAO.create();
    }

    @Override
    public Session getById(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public Session deleteById(String sessionId) {
        return sessions.remove(sessionId);
    }

    @Override
    public void put(String id, Session session) {
        sessions.put(id, session);
    }
}
