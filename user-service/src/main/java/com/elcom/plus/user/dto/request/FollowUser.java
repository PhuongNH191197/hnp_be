package com.elcom.plus.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowUser {
    private int userId;
    private int creatorType;
    private int type;
}
