package com.elcom.plus.auth.dto.response;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthResponse extends AbstractResponse {
    private String accessToken;
    private String refreshToken;
    private UserInfoResponse userInfo;
}
