package com.piesat.site.system.dict.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.anno.Description;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.system.dict.service.DictItemService;
import com.piesat.site.system.dict.service.DictService;
import com.piesat.site.system.dict.service.dto.*;
import com.piesat.site.system.dict.service.entity.SysDict;
import com.piesat.site.system.dict.service.entity.SysDictItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/dictItem")
public class DictItemController {

    @Autowired
    private DictItemService dictItemServiceImpl;

    @Description("查询栏目接口")
    @RequestMapping(value = "/queryDictItemList", method = RequestMethod.GET)
    public Wrapper queryDictItemList(QueryDictItemReqDto queryDictItemReqDto) {
        try {
            PageHelper.startPage(queryDictItemReqDto.getCurrentPage(),queryDictItemReqDto.getPageSize());
            List<SysDictItem> queryDictItemList = dictItemServiceImpl.queryDictItemList(queryDictItemReqDto);
            PageInfo<SysDictItem> pageInfo = new PageInfo<>(queryDictItemList);
            return WrapMapper.ok(pageInfo);
        } catch (Exception e) {
            return WrapMapper.error("查询失败");
        }
    }

    @Description("增加栏目接口")
    @RequestMapping(value = "/addDictItem", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Wrapper addDict(@RequestBody AddDictItemReqDto addDictItemReqDto) throws IOException {
        boolean addDictItem = dictItemServiceImpl.addDictItem(addDictItemReqDto);
        if (addDictItem){
            return WrapMapper.ok();
        }else {
            return WrapMapper.error("保存失败");
        }
    }

    @Description("编辑栏目接口")
    @RequestMapping(value = "/editDictItem", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Wrapper editDictItem(@RequestBody EditDictItemReqDto editDictItemReqDto) {
        boolean editDictItem = dictItemServiceImpl.editDictItem(editDictItemReqDto);
        if (editDictItem) {
            return WrapMapper.ok();
        } else {
            return WrapMapper.error("修改失败");
        }
    }

    @Description("删除栏目接口")
    @RequestMapping(value = "/deleteDictItem", method = RequestMethod.POST)
    public Wrapper deleteDictItem(Integer id) {
        boolean deleteDictItem = dictItemServiceImpl.deleteDictItem(id);
        if (deleteDictItem){
            return WrapMapper.ok();
        }else {
            return WrapMapper.error("删除失败");
        }
    }
}
