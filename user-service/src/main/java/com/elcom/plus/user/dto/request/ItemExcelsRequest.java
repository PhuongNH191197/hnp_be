package com.elcom.plus.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemExcelsRequest {
    private String owner;
    private String title;
    private String fileName;
    private String categoryName;

    private String expirationDays;
    private String price;
    private String avatar;
    private String hashtag;
    private int timeLineVideo;

}
