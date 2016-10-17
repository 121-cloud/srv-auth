package otocloud.auth.dao;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

import javax.inject.Inject;

/**
 * Created by better/zhangye on 15/9/30.
 */

public class SessionDAO {

    @Inject
    private Vertx vertx;

    private SessionStore sessionStore;

    public SessionDAO() {

    }

    public Session create() {
        checkSessionStore();

        Session session = sessionStore.createSession(SessionHandler.DEFAULT_SESSION_TIMEOUT);

        return session;
    }

    public void get(String id, Future<Session> doneFuture){
        sessionStore.get(id, ret -> {
            if (ret.succeeded()){
                Session session = ret.result();
                doneFuture.complete(session);
            }else{
                doneFuture.fail(ret.cause());
            }
        });
    }

    public void put(Session session, Future<Boolean> doneFuture){
        sessionStore.put(session, ret-> {
            if(ret.succeeded()){
                boolean success = ret.result();
                doneFuture.complete(success);
            }else{
                doneFuture.fail(ret.cause());
            }
        });
    }


    private void checkSessionStore() {
        if (sessionStore == null) {
            sessionStore = LocalSessionStore.create(vertx);
        }
    }
}
