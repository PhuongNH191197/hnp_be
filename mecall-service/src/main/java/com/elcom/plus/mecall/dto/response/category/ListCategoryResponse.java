package com.elcom.plus.mecall.dto.response.category;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListCategoryResponse extends AbstractResponse {
    private List<CategoryResponse> data;
}
