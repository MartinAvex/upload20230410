package com.codermy.myspringsecurityplus.admin.service.impl;

import com.codermy.myspringsecurityplus.admin.dao.GenerationDao;
import com.codermy.myspringsecurityplus.admin.dto.GenerationDto;
import com.codermy.myspringsecurityplus.admin.entity.MyGeneration;
import com.codermy.myspringsecurityplus.admin.service.GenerationService;
import com.codermy.myspringsecurityplus.common.utils.PoiUtils;
import com.codermy.myspringsecurityplus.common.utils.Result;
import com.codermy.myspringsecurityplus.common.utils.ResultCode;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Xavier.wu
 * @CreateTime: 2023-04-07  15:52
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class GenerationServiceImpl implements GenerationService {

    @Autowired
    private GenerationDao generationDao;

    @Override
    public List<GenerationDto> getList() {
        return generationDao.getList();
    }

    @Override
    public Result<MyGeneration> getGenerationList(Integer offectPosition, Integer limit, MyGeneration myGeneration) {
        Page page = PageHelper.offsetPage(offectPosition,limit);
        List<MyGeneration> list = generationDao.getGenerationList(myGeneration);
        return Result.ok().count(page.getTotal()).data(list).code(ResultCode.TABLE_SUCCESS);
    }

    @Override
    public Result<MyGeneration> importData(MultipartFile file) {
        if (Objects.isNull(file)) {
            return Result.error().message("没有上传文件，请核实！");
        }
        //先删除历史数据
        generationDao.deleteHistory();

        try {
            List<MyGeneration> generationList = PoiUtils.readExcel(file, (lineNum, rows) -> {
                int i = 0;
                String name = rows[i++]; // 名称
                String gene = rows[i++]; // 基因序列

                MyGeneration product = new MyGeneration();
                product.setName(name);
                product.setGeneration(gene);
                return product;
            });

            generationDao.batchSave(generationList);

            return Result.ok();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer batchRemove() {
        return generationDao.deleteHistory();
    }
}
