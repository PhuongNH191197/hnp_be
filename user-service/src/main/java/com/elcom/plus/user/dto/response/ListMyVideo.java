package com.elcom.plus.user.dto.response;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListMyVideo extends AbstractResponse {
    private List<MyVideoResponse> data;
    private int skipIndex = -1;
}
