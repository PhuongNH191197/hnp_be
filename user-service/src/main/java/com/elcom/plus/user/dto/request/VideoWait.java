package com.elcom.plus.user.dto.request;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoWait extends AbstractResponse {
    private int id;
    private int type;
    private String videoIds;
    private int timeType;
    private String timeFrom;
    private String timeTo;
}
