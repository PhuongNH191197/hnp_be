package com.elcom.plus.user.dto.request;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockPhone extends AbstractResponse {
    private String phone;
    private Integer type; // type = 0 => add | type = 1 => remove
    private String name;
}
