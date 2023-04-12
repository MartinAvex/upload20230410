package com.codermy.myspringsecurityplus.admin.service.impl;

import com.codermy.myspringsecurityplus.admin.dao.GenerationDao;
import com.codermy.myspringsecurityplus.admin.entity.MyGeneration;
import com.codermy.myspringsecurityplus.admin.request.ConditionParameter;
import com.codermy.myspringsecurityplus.admin.request.GenerationRequest;
import com.codermy.myspringsecurityplus.admin.service.GenerationService;
import com.codermy.myspringsecurityplus.common.utils.PageTableRequest;
import com.codermy.myspringsecurityplus.common.utils.PoiUtils;
import com.codermy.myspringsecurityplus.common.utils.Result;
import com.codermy.myspringsecurityplus.common.utils.ResultCode;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Xavier.wu
 * @CreateTime: 2023-04-07  15:52
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class GenerationServiceImpl implements GenerationService {

    private static final String DEFAULT_SORT_FIELD = "create_date";
    @Autowired
    private GenerationDao generationDao;

    @Override
    public Result<MyGeneration> getGenerationList(PageTableRequest pageTable, GenerationRequest param) {
        Page page;
        if (Objects.isNull(pageTable)) {
            page = PageHelper.offsetPage(0, Integer.MAX_VALUE);
        } else {
            page = PageHelper.offsetPage(pageTable.getOffset(), pageTable.getLimit());
        }
        List<MyGeneration> list = generationDao.getGenerationList(getParameter(param));
        return Result.ok().count(page.getTotal()).data(list).code(ResultCode.TABLE_SUCCESS);
    }

    @Override
    public Result<MyGeneration> importData(MultipartFile file) {
        if (Objects.isNull(file)) {
            return Result.error().message("没有上传文件，请核实！");
        }

        try {
            List<MyGeneration> generationList = PoiUtils.readExcel(file, (lineNum, rows) -> {
                String name = rows[5]; // 名称
                String gene = rows[9].replace("[", "").replace("]", ""); // 基因序列

                MyGeneration generation = new MyGeneration();
                generation.setName(name);
                generation.setGeneration(gene);
                return generation;
            });

            generationDao.batchSave(generationList);

            return Result.ok();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer batchRemove(String date) {
        return generationDao.deleteHistory(date);
    }

    private ConditionParameter getParameter(GenerationRequest param) {
        String start = "",  end = "";
        if (StringUtils.isNotEmpty(param.getCreateDate())) {
            String[] dateArr = param.getCreateDate().replace(" ", "").split("~");
            start = dateArr[0];
            end = dateArr[1];
        } else {
            start = end = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        return ConditionParameter
                .builder()
                .start(start)
                .end(end)
                .sortField(characterConvert(param.getField()))
                .order(StringUtils.isEmpty(param.getOrder()) ? "asc" : param.getOrder())
                .build();
    }

    private String characterConvert(String field) {

        if (StringUtils.isEmpty(field)) {
            return DEFAULT_SORT_FIELD;
        }

        Pattern  pattern = Pattern.compile("[A-Z]");
        StringBuilder builder = new StringBuilder(field);
        Matcher matcher = pattern.matcher(field);
        int i=0;
        while (matcher.find()) {
            builder.replace(matcher.start()+i, matcher.end()+i, "_"+matcher.group().toLowerCase());
            i++;
        }
        if('_' == builder.charAt(0)){
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

}
