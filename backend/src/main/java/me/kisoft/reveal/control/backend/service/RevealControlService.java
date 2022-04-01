/*
 */
package me.kisoft.reveal.control.backend.service;

import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsErrorContext;
import io.javalin.websocket.WsMessageContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Tareq Kirresh
 */
public class RevealControlService {

    private static RevealControlService INSTANCE = instance();
    private final Map<String, RevealControlSession> controlSessionMap = new ConcurrentHashMap<>();
    private final Map<String, RevealClientSession> clientSessionMap = new ConcurrentHashMap();

    public static RevealControlService instance() {
        if (INSTANCE == null) {
            INSTANCE = new RevealControlService();
        }
        return INSTANCE;
    }

    public void addSession(WsConnectContext wcc) {
        String sessionType = wcc.queryParam("sessionType");
        if (StringUtils.equalsIgnoreCase(sessionType, "client")) {
            RevealClientSession session = new RevealClientSession();
            session.setSessionContext(wcc);
            clientSessionMap.put(session.getSessionId(), session);
            session.sendSessionInfo();
        } else if (StringUtils.equalsIgnoreCase(sessionType, "controller")) {
            String clientSessionId = wcc.queryParam("sessionId");
            RevealControlSession session = new RevealControlSession();
            session.setClientSessionId(clientSessionId);
            session.setSessionContext(wcc);
            controlSessionMap.put(session.getSessionId(), session);
        }
    }

    public void removeSession(WsCloseContext wcc) {
        this.cleanupSession(wcc);
    }

    public void removeSession(WsErrorContext wec) {
        this.cleanupSession(wec);
    }

    public void handleMessage(WsMessageContext wmc) {
        RevealControlSession controlSession = controlSessionMap.get(wmc.getSessionId());
        if (controlSession != null) {
            RevealClientSession clientSession = clientSessionMap.get(controlSession.getClientSessionId());
            if (clientSession != null) {
                clientSession.sendCommand(wmc.message());
            }
        }

    }

    private void cleanupSession(WsContext wsc) {
        RevealClientSession clientSession = clientSessionMap.values().stream()
                .filter(session -> StringUtils.equals(wsc.getSessionId(), session.getSessionContext().getSessionId()))
                .findFirst()
                .orElse(null);

        RevealControlSession controlSession = controlSessionMap.values().stream()
                .filter(session -> StringUtils.equals(wsc.getSessionId(), session.getSessionContext().getSessionId()))
                .findFirst()
                .orElse(null);

        if (clientSession != null) {
            clientSessionMap.remove(clientSession.getSessionId());
        }
        if (controlSession != null) {
            controlSessionMap.remove(controlSession.getSessionId());
        }
    }
}
