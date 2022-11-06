package com.elcom.plus.vrbt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApsRequest {
    private String msisdn;
    private String package_name;
    private String dynamicSubserviceID;
    private String user;
    private String price;
}
