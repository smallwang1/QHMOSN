package com.piesat.site.datasearch.service.impl;

import com.piesat.site.datasearch.service.IMosnFormService;
import com.piesat.site.datasearch.service.dto.FormDto;
import com.piesat.site.datasearch.service.dto.InterfaceDto;
import com.piesat.site.datasearch.service.dto.TitleDto;
import com.piesat.site.datasearch.service.entity.MosnForm;
import com.piesat.site.datasearch.service.mapper.MosnFormMapper;
import com.piesat.site.datasearch.service.util.BeanUtils;
import com.piesat.site.datasearch.service.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("mosnFormService")
public class MosnFormServiceImpl implements IMosnFormService {

    private static final Logger logger = LoggerFactory.getLogger(MosnFormServiceImpl.class);

    @Autowired
    private MosnFormMapper mosnFormMapper;

    @Override
    public List<FormDto> generatorForm(Long[] formIds, List<InterfaceDto> interfaceList, List<TitleDto> titleList) {
        if (StringUtils.isEmpty(formIds)) return null;
        List<MosnForm> formList = mosnFormMapper.selectFormByIds(formIds);
        if (StringUtils.isEmpty(formList)) return null;
        List<FormDto> formDtos = BeanUtils.convertList2List(formList, FormDto.class);
        Map<Long, List<TitleDto>> titleMap = titleList.stream().collect(Collectors.groupingBy(TitleDto::getFormId));
        for (FormDto formDto : formDtos) {
            if (StringUtils.equals("search", formDto.getFormCode())){
                formDto.setIsSearch("1");
                formDto.setTitleList(interfaceList);
            } else {
                formDto.setIsSearch("0");
                formDto.setTitleList(titleMap.get(formDto.getFormId()));
            }
        }

        return formDtos;
    }
}
