package com.elcom.plus.mecall.dto.response.hashtag;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HashtagResponse extends AbstractResponse {
    private int id;
    private String hashtag;
    private int orderIndex;
}
