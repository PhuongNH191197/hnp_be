package com.elcom.plus.mecall.dto.response.category;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse extends AbstractResponse {
    private long id;
    private Long order;
    private String name;
    private Integer positionDesign;
    private String groupCode;
    private Integer bannerPositionId;
}
