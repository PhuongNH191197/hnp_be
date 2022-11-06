package com.elcom.plus.mecall.dto.response.banner;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListBannerResponse extends AbstractResponse {
    private List<BannerResponse> data;
}
