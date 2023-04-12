package com.codermy.myspringsecurityplus.admin.request;

import lombok.Data;

/**
 * @Author: Xavier.wu
 * @CreateTime: 2023-04-12  15:43
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class GenerationRequest {

    private String createDate;
    private String field;
    private String order;

}
