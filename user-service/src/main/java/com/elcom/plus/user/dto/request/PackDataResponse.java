package com.elcom.plus.user.dto.request;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackDataResponse extends AbstractResponse {
    private int id;
    private String name;
    private String title;
    private String price;
    private String description;
    private int status;
    private String expired;
}
