package com.elcom.plus.mecall.dto.response.top;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VideoTopAll extends AbstractResponse {
    private List<VideoTopResponse> top1;
    private List<VideoTopResponse> top2;
    private List<VideoTopResponse> top3;
}
