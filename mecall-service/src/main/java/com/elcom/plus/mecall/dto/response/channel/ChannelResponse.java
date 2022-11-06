package com.elcom.plus.mecall.dto.response.channel;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelResponse extends AbstractResponse {
    private int id;
    private String name;
    private Integer orderIndex;
    private String avatar;
}
