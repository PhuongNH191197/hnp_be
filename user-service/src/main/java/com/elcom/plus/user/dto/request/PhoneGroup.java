package com.elcom.plus.user.dto.request;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rx.internal.operators.OperatorDoOnUnsubscribe;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneGroup extends AbstractResponse {
    private int id;
    private String phone;
    private String sub;
}
