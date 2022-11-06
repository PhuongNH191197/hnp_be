package com.elcom.plus.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPassword {
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String otp;
}