package com.elcom.plus.auth.entity;

import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private int age;
    private String address;
    private int loginType;
    private int rankId;
    private String avatar;
    private int status;
    private LocalDateTime dateBlock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
