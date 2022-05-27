package com.piesat.site.datasearch.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.quartz.entity.MosnJob;
import com.piesat.quartz.service.IMosnJobService;
import com.piesat.site.datasearch.service.IMosnRecommendDetailService;
import com.piesat.site.datasearch.service.dto.RecommendDetailDto;
import com.piesat.site.datasearch.service.util.BeanUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/recommendDetail")
public class MosnRecommendDetailController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MosnRecommendDetailController.class);

    @Autowired
    private IMosnRecommendDetailService mosnRecommendDetailService;
    @Autowired
    private IMosnJobService mosnJobService;

    /**
     * 推荐产品大类分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list/{pageNum}/{pageSize}")
    public Wrapper list(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize, true);
        List<RecommendDetailDto> recommendDetailDtos = BeanUtils.convertList2List(mosnRecommendDetailService.selectRecommendDetailAll(), RecommendDetailDto.class);
        PageInfo<RecommendDetailDto> detailPageInfo = new PageInfo<>(recommendDetailDtos);
        detailPageInfo.setTotal(page.getTotal());
        return WrapMapper.ok(detailPageInfo);
    }

    @GetMapping("/health")
    public void health() throws SchedulerException {
        MosnJob mosnJob = mosnJobService.selectJobById(3L);
        mosnJobService.run(mosnJob);
    }

    @PostMapping("upload")
    public Wrapper upload(MultipartFile file) throws IOException {
        try(InputStream inputStream = file.getInputStream()) {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (OutputStream out = Base64.getEncoder().wrap(bos)) {
                
            }
        }
        return null;
    }


}
