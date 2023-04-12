package com.codermy.myspringsecurityplus.admin.request;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Xavier.wu
 * @CreateTime: 2023-04-12  15:51
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@Builder
public class ConditionParameter {

    private String start;
    private String end;
    private String sortField;
    private String order;

}
