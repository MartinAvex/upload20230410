package com.codermy.myspringsecurityplus.admin.service;

import com.codermy.myspringsecurityplus.admin.dto.GenerationDto;
import com.codermy.myspringsecurityplus.admin.entity.MyGeneration;
import com.codermy.myspringsecurityplus.common.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: Xavier.wu
 * @CreateTime: 2023-04-07  15:51
 * @Description: TODO
 * @Version: 1.0
 */
public interface GenerationService {

    List<GenerationDto> getList(String createDate);
    Result<MyGeneration> getGenerationList(Integer offectPosition, Integer limit, String createDate);

    Result<MyGeneration> importData(MultipartFile file);
    Integer batchRemove(String date);

}
