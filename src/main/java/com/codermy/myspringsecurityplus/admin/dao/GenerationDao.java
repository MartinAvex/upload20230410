package com.codermy.myspringsecurityplus.admin.dao;

import com.codermy.myspringsecurityplus.admin.entity.MyGeneration;
import com.codermy.myspringsecurityplus.admin.request.ConditionParameter;
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

    List<MyGeneration> getGenerationList(ConditionParameter condition);

    int batchSave(List<MyGeneration> myGenerationList);
    int deleteHistory(String date);

}
