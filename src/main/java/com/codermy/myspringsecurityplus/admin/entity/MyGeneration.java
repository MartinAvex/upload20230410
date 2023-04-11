package com.codermy.myspringsecurityplus.admin.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: Xavier.wu
 * @CreateTime: 2023-04-07  15:38
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class MyGeneration implements Serializable {


    private static final long serialVersionUID = 5146996317420973019L;

    @Id
    private Integer id;

    private String name;

    private String generation;

    private LocalDate createDate;

    public MyGeneration() {
        createDate = LocalDate.now();
    }
}
