package com.elcom.plus.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoRequest {
    private Integer videoId;
    private String msisdn;
    private String msisdnReceiver;
    private Integer id;
}
