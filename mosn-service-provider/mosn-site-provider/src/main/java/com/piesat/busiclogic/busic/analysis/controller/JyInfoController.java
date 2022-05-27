/**
 * 项目名称:analysis
 * 类名称:JyInfoController.java
 * 包名称:com.piesat.busiclogic.busic.controller
 *
 * 修改履历:
 *       日期                   修正者      主要内容
 *  Thu Apr 02 16:30:26 CST 2020       admin         初版做成
 *
 *
 */

package com.piesat.busiclogic.busic.analysis.controller;

import com.piesat.busiclogic.busic.analysis.entity.JyInfo;
import com.piesat.busiclogic.busic.analysis.service.IJyInfoService;
import com.piesat.busiclogic.common.file.FileManager;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.jdbc.Page;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JyInfo相关的Controller,负责解析前台的增、删、改、查等操作。    <br>
 * @author admin
 * @version 1.0
 */
@RestController
@RequestMapping("/jyInfo")
public class JyInfoController {
    @Autowired
    private IJyInfoService iJyInfoService;

    @Value("${file.tempfile}")
    private String tempfile;

    /**
     *
     * 主要功能: 提供保存JyInfo的功能           <br>
     * 注意事项:无           <br>
     *
     * @param   data
     * @return  标准Wrapper
     */
    @ApiOperation(value = "保存一个JyInfo", notes = "")
    @ApiImplicitParam(name = "jyInfo", value = "JyInfo（实体类，参考实体类模型）", required = true, dataType = "JyInfo")
    @ApiResponse(code = 200, message = "标准返回结果,无额外数据。异常时返回标准异常。")
    @RequestMapping("/batchInsert")
    public Wrapper insert(@Valid String  data) {
        ObjectMapper mapper = new ObjectMapper();
        List<JyInfo> list = null;
        try {
            list = mapper.readValue(data, new TypeReference<List<JyInfo>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
       iJyInfoService.batchInsertJyInfo(list);
       return WrapMapper.ok();
    }

    /**
     *
     * 主要功能: 更新JyInfo          <br>
     * 注意事项: 修改时，需要指定主键，要限定前台不可改变。          <br>
     *           后台更新时，如果主键修改过，就无法保证是需求的那条记录。  <br>
     *           更新时，还应该校验记录是否存在，以及用户是否有修改的权限。<br>
     *
     * @param jyInfo JyInfo
     * @return       标准Wrapper
     */
    @ApiOperation(value = "修改一个JyInfo", notes = "")
    @ApiImplicitParam(name = "jyInfo", value = "JyInfo（实体类，参考实体类模型）", required = true, dataType = "JyInfo")
    @ApiResponse(code = 200, message = "标准返回结果,无额外数据。异常时返回标准异常。")
    @RequestMapping("/update")
    public Wrapper update(@Valid JyInfo jyInfo) {
        iJyInfoService.updateJyInfo(jyInfo);
        return WrapMapper.ok();
    }

    /**
     *
     * 主要功能: 根据删除指定JyInfo            <br>
     * 注意事项: 仅按指定项单个删除。 批量删除情况，最好从dao层使用batchUpdate 方法进行批量删除。           <br>
     * @return 标准的Wrapper, 添加JyInfo:jyInfo 键值对。
     */
    @ApiOperation(value = "根据查询指定JyInfo", notes = "")
    @ApiResponse(code = 200, message = "标准返回结果,添加jyInfo:JyInfo键值对。")
    @RequestMapping("/delete")
    public Wrapper delete() {

        iJyInfoService.deleteJyInfo();

    return WrapMapper.ok();

    }

    /**
     *
     * 主要功能: 根据查询指定JyInfo           <br>
     * 注意事项: 无          <br>
     *
     * @return 标准的Wrapper, 添加JyInfo:jyInfo 键值对。
     */
    @ApiOperation(value = "根据查询指定JyInfo", notes = "")
    @ApiResponse(code = 200, message = "标准返回结果,添加jyInfo:JyInfo键值对。")
    @RequestMapping("/findJyInfoById")
    public Wrapper findJyInfoById() {
        JyInfo jyInfo = iJyInfoService.findJyInfoById();
    return WrapMapper.ok(jyInfo);
    }

    /**
     *
     * 主要功能:  分页查询JyInfo         <br>
     * 注意事项:  默认未添加查询条件，仅按分页查询          <br>
     * @param jyInfo jyInfo实体
     * @return 分页数据 Page<JyInfo>
     */
    @ApiOperation(value = "分页查询指定JyInfo", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "currentPage", value = "开始页", required = true),
        @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true),
        @ApiImplicitParam(name = "jyInfo", value = "jyInfo实体", required = false)})
    @ApiResponse(code = 200, message = "标准返回结果,添加page:Page<JyInfo>键值对。")
    @RequestMapping("/findPageJyInfos")
    public Wrapper findPageJyInfos(@RequestParam(value = "currentPage",defaultValue = "1",required = false) int currentPage,
                                   @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,JyInfo jyInfo) {
        Page<JyInfo> pJyInfo = iJyInfoService.findPageJyInfo(currentPage, pageSize,jyInfo);
        return WrapMapper.ok(pJyInfo);
    }

    /**
     * 纪要文件解析入库
     * @param filename
     * @return
     */
    @RequestMapping("/analysis")
    public Wrapper fileAnalysis(@RequestParam String filename){
        String count = iJyInfoService.fileAnalysis(filename);
        return WrapMapper.ok(count);
    }

    /**
     * 纪要文件上传到临时目录
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload")
    @ResponseBody
    public Wrapper jyInfoUpload(@RequestParam("file") MultipartFile file){
        Map<String,String> map = FileManager.upload(file,tempfile);
        return WrapMapper.ok(map);
    }
}
