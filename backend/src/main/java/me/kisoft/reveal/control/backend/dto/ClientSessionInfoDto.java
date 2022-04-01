/*
 */

package me.kisoft.reveal.control.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Tareq Kirresh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientSessionInfoDto {
    private static final String type = "clientSessionInfo";
    private String url;
    private String qrImage;
}
