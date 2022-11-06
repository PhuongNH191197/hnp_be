package com.elcom.plus.auth.dto.response;

import com.elcom.plus.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String username;
    private String phone;
    private int loginType;
    private String email;
    private int status;
    private String avatar;

    private int age;
    private String address;
    private int rankId;
    private LocalDateTime dateBlock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserInfoResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.phone = user.getPhone();
        this.loginType = user.getLoginType();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.avatar = user.getAvatar();
        this.age = user.getAge();
        this.address = user.getAddress();
        this.rankId = user.getRankId();
        this.dateBlock = user.getDateBlock();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
