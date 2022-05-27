package com.piesat.site.system.dict.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.anno.Description;
import com.piesat.common.core.dto.LoginAuthDto;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.system.customcolumn.service.dto.EditColumnReqDto;
import com.piesat.site.system.dict.service.DictService;
import com.piesat.site.system.dict.service.dto.AddDictReqDto;
import com.piesat.site.system.dict.service.dto.EditDictReqDto;
import com.piesat.site.system.dict.service.dto.QueryDictReqDto;
import com.piesat.site.system.dict.service.entity.SysDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/api/dict")
public class DictController {

    @Autowired
    private DictService dictServiceImpl;

    @Description("查询字典接口")
    @RequestMapping(value = "/queryDictList", method = RequestMethod.GET)
    public Wrapper queryDictList(QueryDictReqDto queryDictReqDto) {
        try {
            PageHelper.startPage(queryDictReqDto.getCurrentPage(),queryDictReqDto.getPageSize());
            List<SysDict> queryDictList = dictServiceImpl.queryDictList(queryDictReqDto);
            PageInfo<SysDict> pageInfo = new PageInfo<>(queryDictList);
            return WrapMapper.ok(pageInfo);
        } catch (Exception e) {
            return WrapMapper.error("查询失败");
        }
    }

    @Description("增加字典接口")
    @RequestMapping(value = "/addDict", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Wrapper addDict(@RequestBody AddDictReqDto addDictReqDto) throws IOException {
        boolean addDict = dictServiceImpl.addDict(addDictReqDto);
        if (addDict){
            return WrapMapper.ok();
        }else {
            return WrapMapper.error("保存失败");
        }
    }

    @Description("编辑字典接口")
    @RequestMapping(value = "/editDict", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Wrapper editDict(@RequestBody EditDictReqDto editDictReqDto) {
        boolean editDict = dictServiceImpl.editDict(editDictReqDto);
        if (editDict) {
            return WrapMapper.ok();
        } else {
            return WrapMapper.error("修改失败");
        }
    }

    @Description("禁用字典接口")
    @RequestMapping(value = "/banDict", method = RequestMethod.POST)
    public Wrapper banDict(String dictCode) {
        boolean banDict = dictServiceImpl.banDict(dictCode);
        if (banDict){
            return WrapMapper.ok();
        }else {
            return WrapMapper.error("删除失败");
        }
    }

    @Description("启用栏目接口")
    @RequestMapping(value = "/startDict", method = RequestMethod.POST)
    public Wrapper startDict(String dictCode) {
        boolean startDict = dictServiceImpl.startDict(dictCode);
        if (startDict){
            return WrapMapper.ok();
        }else {
            return WrapMapper.error("删除失败");
        }
    }

    @Description("测试接口")
    @RequestMapping(value = "/startDict", method = RequestMethod.POST)
    public Wrapper test(String dictCode) {
        boolean startDict = dictServiceImpl.startDict(dictCode);
        if (startDict){
            return WrapMapper.ok();
        }else {
            return WrapMapper.error("删除失败");
        }
        //排序


    }
}
