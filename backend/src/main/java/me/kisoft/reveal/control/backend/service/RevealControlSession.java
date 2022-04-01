/*
 */

package me.kisoft.reveal.control.backend.service;

import io.javalin.websocket.WsContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Tareq Kirresh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevealControlSession {
    
    private WsContext sessionContext;
    
    private String clientSessionId;

    public String getSessionId() {
        return sessionContext.getSessionId();
    }
    
    
}
