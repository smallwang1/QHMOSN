package com.piesat.site.identifier.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.datasearch.service.entity.MosnResource;
import com.piesat.site.datasearch.service.util.BeanUtils;
import com.piesat.site.identifier.service.IMosnServiceOidService;
import com.piesat.site.identifier.service.dto.ServiceOidDto;
import com.piesat.site.identifier.service.entity.MosnServiceOidEntity;
import com.piesat.site.identifier.service.vo.ServiceOidVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dataOid")
public class MosnDataOidController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MosnDataOidController.class);

    @Autowired
    private IMosnServiceOidService mosnServiceOidService;

    /**
     * 数据申请分页查询
     * @param pageNum
     * @param pageSize
     * @param serviceOid
     * @return
     */
    @GetMapping("/list/{pageNum}/{pageSize}")
    public Wrapper list(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize, @RequestBody(required = false) ServiceOidVo serviceOid) {
        PageHelper.startPage(pageNum, pageSize);
        List<ServiceOidDto> serviceOidList = BeanUtils.convertList2List(mosnServiceOidService.selectServiceOidList(serviceOid), ServiceOidDto.class);
        PageInfo<ServiceOidDto> serviceOidPageInfo = new PageInfo<>(serviceOidList);
        return WrapMapper.ok(serviceOidPageInfo);
    }

    /**
     * 操作人提交
     * @param serviceOid
     * @return
     */
    @PostMapping("/operAdd")
    public Wrapper operAdd(@Validated @RequestBody ServiceOidVo serviceOid){
        return WrapMapper.ok(mosnServiceOidService.insertServiceOid(BeanUtils.convertObj2Obj(serviceOid, MosnServiceOidEntity.class)));
    }

    /**
     * 修改保存数据申请
     * @param serviceOid
     * @return
     */
    @PutMapping
    public Wrapper edit(@Validated @RequestBody ServiceOidDto serviceOid) {
//        LoginAuthDto loginAuthDto = this.getLoginAuthDto();
//        resource.setCreateBy(loginAuthDto.getUsername());
        try {
            mosnServiceOidService.updateServiceOid(serviceOid);
            mosnServiceOidService.dataApply(serviceOid);
        } catch (Exception e) {
            return WrapMapper.error(e.getMessage());
        }
        return WrapMapper.ok();
    }

    /**
     * 标识符申请
     * @param serviceOid
     * @return
     */
    @PostMapping("/apply")
    public Wrapper apply(@Validated @RequestBody ServiceOidDto serviceOid) {
        mosnServiceOidService.dataApply(serviceOid);
        return WrapMapper.ok();
    }
}
