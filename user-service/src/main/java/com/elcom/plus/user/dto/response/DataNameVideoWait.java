package com.elcom.plus.user.dto.response;

import com.elcom.plus.common.util.response.AbstractResponse;
import com.elcom.plus.user.dto.request.NameVideoWait;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataNameVideoWait extends AbstractResponse {
    private List<NameVideoWaitSetting> listPhone;
    private List<NameVideoWaitSetting> listGroup;
}
