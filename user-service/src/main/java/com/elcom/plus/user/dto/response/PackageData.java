package com.elcom.plus.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageData {
    private int id;
    private String name;
    private String title;
    private String price;
    private String description;
    private int status;
    private String expired;
}
