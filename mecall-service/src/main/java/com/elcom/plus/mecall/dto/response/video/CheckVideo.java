package com.elcom.plus.mecall.dto.response.video;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckVideo extends AbstractResponse {
    private Boolean status;
}
