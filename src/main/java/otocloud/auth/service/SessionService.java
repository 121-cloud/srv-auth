package otocloud.auth.service;

import com.google.inject.ImplementedBy;
import io.vertx.ext.web.Session;

/**
 * Created by better/zhangye on 15/9/29.
 */
@ImplementedBy(SessionServiceImpl.class)
public interface SessionService {

    Session createSession();

    Session getById(String sessionId);
    Session deleteById(String sessionId);

    void put(String key, Session session);

}
