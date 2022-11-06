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
public class Post {
    private Long id;
    private String contentPost;
    private int addressId;
    private int status;
    private int likeCount;
    private int cmtCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int orderIndex;
}

