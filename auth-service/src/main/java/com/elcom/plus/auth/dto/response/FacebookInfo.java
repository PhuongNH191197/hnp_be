package com.elcom.plus.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacebookInfo {
    private FacebookPicture picture;
    private String name;
    private String id;
}
