package com.lcc.cloist.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MultipleResult<T> extends CommonResult {
    private List<T> data;
}
