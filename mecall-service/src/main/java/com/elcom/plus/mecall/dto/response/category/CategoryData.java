package com.elcom.plus.mecall.dto.response.category;


import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryData extends AbstractResponse {
    private List<ItemVideoResponse> listVideoData;
//    private ItemVideoResponse videoDetail;
    private int skipIndex;
}
