package com.elcom.plus.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MTsms {
    private String msisdn;
    private String smsContent;
}
