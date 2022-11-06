package com.elcom.plus.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacebookPictureInfo {
    private int height;
    private boolean is_silhouette;
    private String url;
    private int width;
}
