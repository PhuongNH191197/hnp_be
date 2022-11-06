package com.elcom.plus.auth.dto.response;

import com.elcom.plus.auth.entity.Post;
import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse extends AbstractResponse {
    private Long id;
    private String contentPost;
    private int addressId;
    private int status;
    private int likeCount;
    private int cmtCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int orderIndex;

    public PostResponse(Post post) {
        this.id = id;
        this.contentPost = post.getContentPost();
        this.addressId = post.getAddressId();
        this.status = post.getStatus();
        this.likeCount = post.getLikeCount();
        this.cmtCount = post.getCmtCount();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.orderIndex = post.getOrderIndex();
    }
}
