package com.piesat.site.system.customcolumn.controller;

import com.piesat.common.anno.Description;
import com.piesat.common.core.dto.LoginAuthDto;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.system.customcolumn.service.ColumnService;
import com.piesat.site.system.customcolumn.service.dto.AddColumnReqDto;
import com.piesat.site.system.customcolumn.service.dto.QueryColumnReqDto;
import com.piesat.site.system.customcolumn.service.dto.EditColumnReqDto;
import com.piesat.site.system.customcolumn.service.entity.ColumnData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author Thomas 2022/1/5 22:49
 * The world of programs is a wonderful world
 */
@RestController
@RequestMapping("/column")
public class ColumnController extends BaseController {

    @Autowired
    private ColumnService columnServiceImpl;

    @Description("查询栏目接口")
    @RequestMapping(value = "/queryColumnList", method = RequestMethod.GET)
    public Wrapper queryColumnList(QueryColumnReqDto queryColumnReqDto) {
        try {
            List<ColumnData> queryColumnList = columnServiceImpl.queryColumnList(queryColumnReqDto);
            return WrapMapper.ok(queryColumnList);
        } catch (Exception e) {
            return WrapMapper.error("查询失败");
        }
    }

    @Description("增加栏目接口")
    @RequestMapping(value = "/addColumn", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Wrapper addColumn(@RequestBody AddColumnReqDto addColumnReqDto, @RequestParam("imgFile")MultipartFile file) throws IOException {
        LoginAuthDto loginAuthDto = this.getLoginAuthDto();
        addColumnReqDto.setCreateUser(String.valueOf(loginAuthDto.getUserId()));
//        AddColumnReqDto addColumnReqDto = new AddColumnReqDto();
        addColumnReqDto.setCreateUser("admin");
        addColumnReqDto.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        String filename = file.getOriginalFilename();
        File imgFile = new File(filename);
//        File imgFile = new File("C://Users//admin//Desktop//封面.png");
        FileInputStream fis = new FileInputStream(imgFile);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        String base64 = new BASE64Encoder().encode(buffer);
        addColumnReqDto.setColumnPng(base64.getBytes());
//        addColumnReqDto.setColumnId(22);
//        addColumnReqDto.setColumnLink("ceshi");
//        addColumnReqDto.setColumnName("ceshi");
//        addColumnReqDto.setColumnStatus("1");
//        addColumnReqDto.setColumnUrl("ceshi");
        boolean addColumn = columnServiceImpl.addColumn(addColumnReqDto);
        if (addColumn){
            return WrapMapper.ok();
        }else {
            return WrapMapper.error("保存失败");
        }
    }

    @Description("编辑栏目接口")
    @RequestMapping(value = "/editColumn", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Wrapper editColumn(EditColumnReqDto editShareReqDto, MultipartFile file) {
        LoginAuthDto loginAuthDto = this.getLoginAuthDto();
        editShareReqDto.setUpdateUser(String.valueOf(loginAuthDto.getUserId()));
        editShareReqDto.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        boolean editColumn = columnServiceImpl.editColumn(editShareReqDto);
        if (editColumn) {
            return WrapMapper.ok();
        } else {
            return WrapMapper.error("修改失败");
        }
    }

    @Description("删除栏目接口")
    @RequestMapping(value = "/deleteColumn", method = RequestMethod.GET)
    public Wrapper deleteColumn(Integer columnId) {
        boolean deleteColumn = columnServiceImpl.deleteColumn(columnId);
        if (deleteColumn){
            return WrapMapper.ok();
        }else {
            return WrapMapper.error("删除失败");
        }
    }
}
