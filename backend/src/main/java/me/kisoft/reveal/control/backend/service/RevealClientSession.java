/*
 */
package me.kisoft.reveal.control.backend.service;

import io.javalin.websocket.WsContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kisoft.reveal.control.backend.dto.ClientSessionInfoDto;
import net.glxn.qrgen.javase.QRCode;

/**
 *
 * @author Tareq Kirresh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevealClientSession {

    private final String sessionId = UUID.randomUUID().toString();

    private WsContext sessionContext;

    private String getControlUrl() {
        try {
            String slideshowUrl = sessionContext.queryParam("slideshowUrl");
            String appendQuery = String.format("sessionId=%s&control=true", sessionId);
            URI oldUri = new URI(slideshowUrl);

            String newQuery = oldUri.getQuery();
            if (newQuery == null) {
                newQuery = appendQuery;
            } else {
                newQuery += "&" + appendQuery;
            }

            return new URI(oldUri.getScheme(), oldUri.getAuthority(),
                    oldUri.getPath(), newQuery, oldUri.getFragment()).toString();
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String generateQrBase64(String url) {
        return Base64.getEncoder().encodeToString(QRCode.from(url)
                .withSize(350, 350)
                .stream().toByteArray());
    }

    public void sendSessionInfo() {
        String controlUrl = getControlUrl();
        sessionContext.send(new ClientSessionInfoDto(controlUrl, generateQrBase64(controlUrl)));
    }

    void sendCommand(String message) {
        sessionContext.send(message);
    }
}
