package com.elcom.plus.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String phone;
    private int loginType;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int status;
    private int channel;
    private String avatar;
    private int autoPlayVideo;
    private int earnMoney;
    private int policy;
    private int followings;
    private int followers;
    private Long omapUserId;
}
