package com.elcom.plus.vrbt.dto.response;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackDataResponse extends AbstractResponse {
    private long id;
    private String title;
    private int price;
    private String description;
    private int type;
    private String registrationUseTime;
    private String moContent;
}
