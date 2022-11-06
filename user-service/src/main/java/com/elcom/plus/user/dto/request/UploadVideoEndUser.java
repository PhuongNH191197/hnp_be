package com.elcom.plus.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadVideoEndUser {
    private int userId;
    private String title;
    private String description;
    private String pathVideo;
    private int expirationDays;
    private String avatar;
    private String hashtags;
    private int timeVideo;
    private int status;
    private int vertical;
}
