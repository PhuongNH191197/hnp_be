package com.elcom.plus.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MpsResponse {
    private Integer error_code;
    private String errot_desc;
    private String object;
}
