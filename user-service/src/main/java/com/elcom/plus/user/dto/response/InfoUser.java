package com.elcom.plus.user.dto.response;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoUser extends AbstractResponse {
    private String username;
    private String avatar;
    private int followings;
    private int followers;
    private int status;
    private List<MyVideoResponse> listVideoData;
    private VideoDetailResponse videoDetail;
}
