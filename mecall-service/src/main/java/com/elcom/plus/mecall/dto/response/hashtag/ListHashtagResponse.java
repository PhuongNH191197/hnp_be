package com.elcom.plus.mecall.dto.response.hashtag;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListHashtagResponse extends AbstractResponse {
    List<HashtagResponse> data;
}
