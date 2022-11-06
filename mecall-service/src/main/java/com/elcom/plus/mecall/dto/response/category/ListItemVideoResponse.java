package com.elcom.plus.mecall.dto.response.category;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListItemVideoResponse extends AbstractResponse {
    private List<ItemVideoResponse> data;
    private int skipIndex = -1;
}
