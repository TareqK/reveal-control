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
@NoArgsConstructor
@AllArgsConstructor
public class RevealSlideUpdateDto {
    private static final String type = "slideUpdate";
    private int verticalIndex;
    private int horizontalIndex;
}

