package me.kisoft.reveal.control.backend.run;

import io.javalin.Javalin;
import me.kisoft.reveal.control.backend.handler.ClientSocketHandler;

/**
 *
 * @author Tareq Kirresh
 */
public class App {
    
    public void run(String[] args){
        Javalin.create().ws("reveal-control", (ws)->{
            ws.onConnect(ClientSocketHandler::onConnect);
            ws.onClose(ClientSocketHandler::onClose);
            ws.onMessage(ClientSocketHandler::onMessage);
            ws.onError(ClientSocketHandler::onError);
        }).start();
    }
}
