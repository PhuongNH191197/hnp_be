package com.elcom.plus.user.dto.response;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse extends AbstractResponse {
    private  int id;
    private String title;
    private Integer linkType;
    private String time;
    private int status;
    private String thumbnail;
    private String linkDetail;
    private int creatorType;
    private int positionDesign;
    private String catName;
}
