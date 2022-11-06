package com.elcom.plus.user.dto.request;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupPhoneDel extends AbstractResponse {
    private int contactId;
    private int id;
}
