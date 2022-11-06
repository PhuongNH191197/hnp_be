package com.elcom.plus.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageReturn {
    private Integer error_code;
    private String errot_desc;
    private String object;
}
