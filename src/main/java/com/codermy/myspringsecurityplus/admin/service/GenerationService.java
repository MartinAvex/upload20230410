package com.codermy.myspringsecurityplus.admin.service;

import com.codermy.myspringsecurityplus.admin.entity.MyGeneration;
import com.codermy.myspringsecurityplus.admin.request.GenerationRequest;
import com.codermy.myspringsecurityplus.common.utils.PageTableRequest;
import com.codermy.myspringsecurityplus.common.utils.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Xavier.wu
 * @CreateTime: 2023-04-07  15:51
 * @Description: TODO
 * @Version: 1.0
 */
public interface GenerationService {

    Result<MyGeneration> getGenerationList(PageTableRequest pageTable, GenerationRequest param);
    Result<MyGeneration> importData(MultipartFile file);
    Integer batchRemove(String date);

}
