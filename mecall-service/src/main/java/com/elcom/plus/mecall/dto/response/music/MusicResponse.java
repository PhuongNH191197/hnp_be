package com.elcom.plus.mecall.dto.response.music;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicResponse extends AbstractResponse {
    private int id;
    private String songName;
    private String songPath;
    private String singerName;
    private String thumbnail;
    private int duration;
}
