package com.elcom.plus.mecall.dto.response.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryGroupResponse {
    private int code;
    private String msg;
    private String backgroundUrl;
    private List<CategoryResponse> data;
}
