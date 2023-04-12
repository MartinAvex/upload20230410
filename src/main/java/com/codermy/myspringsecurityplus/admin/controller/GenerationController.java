package com.codermy.myspringsecurityplus.admin.controller;

import com.codermy.myspringsecurityplus.admin.entity.MyGeneration;
import com.codermy.myspringsecurityplus.admin.request.GenerationRequest;
import com.codermy.myspringsecurityplus.admin.service.GenerationService;
import com.codermy.myspringsecurityplus.common.utils.PageTableRequest;
import com.codermy.myspringsecurityplus.common.utils.PoiUtils;
import com.codermy.myspringsecurityplus.common.utils.Result;
import com.codermy.myspringsecurityplus.log.aop.MyLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author codermy
 * @createTime 2020/7/10
 */
@Controller
@RequestMapping("/api/generation")
@Api(tags = "系统：突变管理")
public class GenerationController {
    @Autowired
    private GenerationService generationService;


    @GetMapping("/index")
    @PreAuthorize("hasAnyAuthority('generation:list')")
    public String index() {
        return "system/generation/generation";
    }

    @RequestMapping
    @ResponseBody
    @ApiOperation(value = "突变列表")
    @PreAuthorize("hasAnyAuthority('generation:list')")
    @MyLog("查询突变数据")
    public Result<MyGeneration> generationList(PageTableRequest pageTableRequest, GenerationRequest param) {
        pageTableRequest.countOffset();
        return generationService.getGenerationList(pageTableRequest, param);
    }

    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @ApiOperation(value = "突变数据导入")
    @PreAuthorize("hasAnyAuthority('generation:list')")
    @MyLog("突变数据导入")
    public Result<MyGeneration> importData(@RequestPart(value = "file") MultipartFile file) {
        return generationService.importData(file);
    }

    @DeleteMapping(value = "/batchRemove")
    @ResponseBody
    @ApiOperation(value = "突变数据删除")
    @PreAuthorize("hasAnyAuthority('generation:list')")
    @MyLog("突变数据删除")
    public Result<Integer> batchRemove() {
        generationService.batchRemove(null);
        return Result.ok();
    }

    @GetMapping(value = "/download")
    @ResponseBody
    @ApiOperation(value = "突变数据导出")
    @PreAuthorize("hasAnyAuthority('generation:list')")
    @MyLog("突变数据导出")
    public void download(HttpServletResponse response, GenerationRequest param) throws IOException {
        Result<MyGeneration> result = generationService.getGenerationList(null, param);
        List<MyGeneration> list = result.getData();
        List<Map<String, Object>> dataList = list.stream().map(e -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", e.getName());
            map.put("gene", e.getGeneration());
            map.put("date", e.getCreateDate());
            return map;
        }).collect(Collectors.toList());
        Map<String, String> header = new LinkedHashMap<>();
        header.put("name", "姓名");
        header.put("gene", "基因序列");
        header.put("date", "导入时间");
        PoiUtils.writeExcel(response, "突变数据.xlsx",header ,dataList);
    }



}

