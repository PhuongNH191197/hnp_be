package com.elcom.plus.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private int id;
    private Long postId;
    private Long userId;
    private String contentCmt;
    private int status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
