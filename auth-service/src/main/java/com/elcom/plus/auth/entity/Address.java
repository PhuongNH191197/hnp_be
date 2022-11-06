package com.elcom.plus.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private int id;
    private int cityId;
    private int districtId;
    private int wardId;
    private String note;
}
