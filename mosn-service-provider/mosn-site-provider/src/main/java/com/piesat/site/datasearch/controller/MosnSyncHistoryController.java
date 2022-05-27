package com.piesat.site.datasearch.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.datasearch.service.IMosnSyncHistoryService;
import com.piesat.site.datasearch.service.dto.SyncHisDto;
import com.piesat.site.datasearch.service.util.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/syncHis")
public class MosnSyncHistoryController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MosnOrderController.class);

    @Autowired
    private IMosnSyncHistoryService mosnSyncHistoryService;

    @GetMapping("/list/{pageNum}/{pageSize}")
    public Wrapper list(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize, @RequestParam("orderNo") String orderNo) {
        Page page = PageHelper.startPage(pageNum, pageSize, true);
        List<SyncHisDto> syncHisList = BeanUtils.convertList2List(mosnSyncHistoryService.selectSyncHisList(orderNo), SyncHisDto.class);
        PageInfo<SyncHisDto> historyPageInfo = new PageInfo<>(syncHisList);
        historyPageInfo.setTotal(page.getTotal());
        return WrapMapper.ok(historyPageInfo);
    }
}
