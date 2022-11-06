package com.elcom.plus.auth.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    private String username;
    private String password;
    @NonNull
    private int loginType;
}
