package com.elcom.plus.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadVideo {
    private String title;
    private String description;
    private Integer isShare;
    private String hashtags;
    private Integer duration;
    private String text;
    private MusicResponse musicSelected;
    private MusicTextDto textEditor;
    private MusicCropDto musicCrop;
}
