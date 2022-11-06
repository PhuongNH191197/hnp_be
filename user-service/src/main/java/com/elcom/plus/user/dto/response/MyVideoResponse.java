package com.elcom.plus.user.dto.response;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyVideoResponse extends AbstractResponse {
    private int itemId;
    private int userId;
    private int creatorType;
    private String creatorAvatar;
    private String thumbnail;
    private Integer catId;
    private String title;
    private List<String> hashtags;
    private Long price;
    private Integer settingId;
    private Long numberView;
    private Long numberLike;
    private String filePath;
    private String fileName;
    private String phoneGift;
    private String username;
    private String supplier;
    private int status;
    private int vertical;
    private int isLike;
    private String expireTime;
}
