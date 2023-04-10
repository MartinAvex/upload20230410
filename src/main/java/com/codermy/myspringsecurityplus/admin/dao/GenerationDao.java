package com.codermy.myspringsecurityplus.admin.dao;

import com.codermy.myspringsecurityplus.admin.dto.GenerationDto;
import com.codermy.myspringsecurityplus.admin.entity.MyGeneration;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Xavier.wu
 * @CreateTime: 2023-04-07  15:47
 * @Description: TODO
 * @Version: 1.0
 */
@Mapper
public interface GenerationDao {

    List<GenerationDto> getList();
    List<MyGeneration> getGenerationList(MyGeneration myGeneration);

    int batchSave(List<MyGeneration> myGenerationList);
    int deleteHistory();

}
