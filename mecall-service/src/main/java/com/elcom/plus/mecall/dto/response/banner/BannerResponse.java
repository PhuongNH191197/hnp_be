package com.elcom.plus.mecall.dto.response.banner;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerResponse extends AbstractResponse {
    private Integer id;
    private String mediaSource;
    private String title;
    private String description;
    private Integer positionId;
    private Integer type;
    private Integer videoId;
    private String mediaLink;
    private Integer catId;
}
