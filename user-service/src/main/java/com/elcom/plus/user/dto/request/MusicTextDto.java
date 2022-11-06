package com.elcom.plus.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicTextDto {
    private String text;
    private String textColor;
    private String textAlign;
    private String bgColor;
    private String font;
    private Integer colorSelected;
    private boolean colorTypeA;
    private float posX;
    private float posY;
    private int videoW;
    private int videoH;
}
