package com.codermy.myspringsecurityplus.admin.entity;

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
@NoArgsConstructor
public class MyGeneration implements Serializable {


    private static final long serialVersionUID = 5146996317420973019L;

    @Id
    private Integer id;

    private String name;

    private String generation;



}
