package com.elcom.plus.mecall.dto.response.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HeaderResponse {
    private String id;
    private String phone;
    private String email;
    private int loginType;
    private int status;
}
