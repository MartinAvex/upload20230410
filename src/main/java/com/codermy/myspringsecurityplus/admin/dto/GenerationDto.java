package com.codermy.myspringsecurityplus.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @Author: Xavier.wu
 * @CreateTime: 2023-04-07  15:38
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class GenerationDto implements Serializable {

    private String name;

    private String generation;



}
