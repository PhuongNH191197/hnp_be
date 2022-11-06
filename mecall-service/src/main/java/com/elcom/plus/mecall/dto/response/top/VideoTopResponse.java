package com.elcom.plus.mecall.dto.response.top;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoTopResponse extends AbstractResponse {
    private int id;
    private int type;
    private int order;
    private int itemId;
    private String fileName;
    private String creatorAvatar;
    private String thumbnail;
    private Time timeSet;
    private int status;
    private String filePath;
    private String creditName;
    private List<String> hashtags;
    private Long price;
    private int numberView = 0;
    private int numberLike = 0;
    private int numberShare = 0;
    private String title;
    private String username;
    private int userId;
    private int creatorType;
    private Integer settingId;
    private String description;
    private String supplier;
    private int isLike;
    private int catId;
}
