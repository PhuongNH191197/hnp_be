package com.elcom.plus.vrbt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApsRegisterResponse {
    private int error_code;
    private String error_desc;
    private Object object;
}
