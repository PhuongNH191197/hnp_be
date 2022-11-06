package com.elcom.plus.user.dto.response;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowsUser extends AbstractResponse {
    private int userId;
    private int creatorType;
    private String username;
    private String avatar;
    private int status;
}
