package com.elcom.plus.mecall.dto.response.cp;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CpResponse extends AbstractResponse {
    private int id;
    private String avatar;
    private String username;
}
