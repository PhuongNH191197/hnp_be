package com.elcom.plus.mecall.dto.response.category;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDetailResponse extends AbstractResponse {
    private int id;
    private String title;
    private String creatorAvatar;
    private String thumbnail;
    private List<String> hashtags;
    private Long price;
    private Integer settingId;
    private Long numberView;
    private Long numberLike;
    private String filePath;
    private String description;
    private String supplier;
    private Long catId;
    private int isLike;
}
