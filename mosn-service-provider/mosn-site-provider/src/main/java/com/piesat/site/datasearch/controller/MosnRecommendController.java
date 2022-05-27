package com.piesat.site.datasearch.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.datasearch.service.IMosnRecommendService;
import com.piesat.site.datasearch.service.dto.RecommendDto;
import com.piesat.site.datasearch.service.entity.MosnRecommend;
import com.piesat.site.datasearch.service.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/recommend")
public class MosnRecommendController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MosnRecommendController.class);

    @Autowired
    private IMosnRecommendService mosnRecommendService;

    /**
     * 推荐产品大类分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list/{pageNum}/{pageSize}")
    public Wrapper list(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize, true);
        List<RecommendDto> recommendDtos = mosnRecommendService.selectRecommendAll();
        PageInfo<RecommendDto> recommendPageInfo = new PageInfo<>(recommendDtos);
        recommendPageInfo.setTotal(page.getTotal());
        return WrapMapper.ok(recommendPageInfo);
    }

    /**
     * 新增推荐大类
     * @param
     * @return
     */
    @PostMapping
    public Wrapper add(@RequestParam("file") MultipartFile fileImage, @RequestParam("recommendName") String recommendName, @RequestParam("detailId") String detailId) throws IOException {
        String fileType = fileImage.getOriginalFilename().substring(fileImage.getOriginalFilename().lastIndexOf(".") + 1, fileImage.getOriginalFilename().length());
        if (!"jpg,jpeg,gif,png".toUpperCase().contains(fileType.toUpperCase())) {
            return WrapMapper.error("请选择jpg,jpeg,gif,png格式的图片");
        }
        return WrapMapper.judge(mosnRecommendService.add(recommendName, detailId, StringUtils.getImageStr(fileImage.getInputStream(), fileType)));
    }

    /**
     * 推荐大类更新
     * @param
     * @return
     */
    @PutMapping("/{id}")
    public Wrapper update(@PathVariable("id") Long id, @RequestParam("file") MultipartFile fileImage, @RequestParam("recommendName") String recommendName, @RequestParam("detailId") String detailId) throws IOException {
        String fileType = fileImage.getOriginalFilename().substring(fileImage.getOriginalFilename().lastIndexOf(".") + 1, fileImage.getOriginalFilename().length());
        if (!"jpg,jpeg,gif,png".toUpperCase().contains(fileType.toUpperCase())) {
            return WrapMapper.error("请选择jpg,jpeg,gif,png格式的图片");
        }
        String imageStr = StringUtils.getImageStr(fileImage.getInputStream(), fileType);
        return WrapMapper.judge(mosnRecommendService.edit(id, imageStr, recommendName, detailId));
    }

    /**
     * 删除推荐大类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Wrapper remove(@PathVariable("id") Long id) {
        return WrapMapper.judge(mosnRecommendService.deleteRecommend(id));
    }
}
