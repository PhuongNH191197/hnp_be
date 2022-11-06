package com.elcom.plus.user.dto.response;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckUserBlock extends AbstractResponse {
    private int type; // 0: chua block user | 1: da block user
    private int userId;
    private int creatorType;
}
