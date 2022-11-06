package com.elcom.plus.user.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterSubscribers {
    private String msisdn;
    private String package_name;
    private String dynamicSubserviceID;
    private String user;
    private String price;
    private String msisdnReceiver;
    private String supplier;
}
