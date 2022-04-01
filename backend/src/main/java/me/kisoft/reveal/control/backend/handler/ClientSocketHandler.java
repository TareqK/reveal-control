/*
 */
package me.kisoft.reveal.control.backend.handler;

import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsErrorContext;
import io.javalin.websocket.WsMessageContext;
import me.kisoft.reveal.control.backend.service.RevealControlService;

/**
 *
 * @author Tareq Kirresh
 */
public class ClientSocketHandler {

    public static void onConnect(WsConnectContext wcc) {
        RevealControlService.instance().addSession(wcc);
    }

    public static void onMessage(WsMessageContext wmc) {
        RevealControlService.instance().handleMessage(wmc);
    }

    public static void onClose(WsCloseContext wcc) {
        RevealControlService.instance().removeSession(wcc);
    }

    public static void onError(WsErrorContext wec) {
        RevealControlService.instance().removeSession(wec);
    }
}
