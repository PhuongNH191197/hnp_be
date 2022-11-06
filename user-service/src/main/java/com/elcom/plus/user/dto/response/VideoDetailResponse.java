package com.elcom.plus.user.dto.response;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDetailResponse extends AbstractResponse {
    private Long catId;
    private Long itemId;
    private String creatorAvatar;
    private String thumbnail;
    private String title;
    private String username;
    private String fileName;
    private String filePath;
    private List<String> hashtags;
    private Long price;
    private int numberView;
    private int numberLike;
    private int numberShare;
    private String supplier;
    private int isLike;
    private int userId;
    private int creatorType;
    private String phoneGift;
}
