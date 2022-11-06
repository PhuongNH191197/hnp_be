package com.elcom.plus.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccount {
    private String authuser;
    private String code;
    private String hd;
    private String prompt;
    private String scope;
    private String state;
}
