package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.dto.ResourceDto;
import com.piesat.site.datasearch.service.entity.MosnResource;

import java.util.List;

public interface IMosnResourceService {

    /**
     * 查询资料动态检索表单
     * @param resourceId
     */
    ResourceDto getDynamicForm(Long resourceId);

    /**
     * 查询产品资料集合
     * @param resource
     * @return
     */
    List<MosnResource> selectResourceList(MosnResource resource);

    /**
     * 根据资料ids获取资料集合
     * @param resouceArr
     * @return
     */
    List<ResourceDto> selectResourceByIds(String resouceArr);

    /**
     * 根据Id查询资料信息
     * @param resourceId
     * @return
     */
    MosnResource selectResourceById(Long resourceId);

    /**
     * 根据菜单Id查询产品集合
     * @param menuId
     * @return
     */
    List<ResourceDto> selectResourceByMenuId(Long menuId);

    /**
     * 新增产品资料
     * @param resource
     * @return
     */
    Integer insertResource(MosnResource resource);

    /**
     * 修改保存产品资料
     * @param resource
     * @return
     */
    Integer updateResource(MosnResource resource);

    /**
     * 删除产品资料
     * @param resourceIds
     * @return
     */
    Integer deleteResource(Long[] resourceIds);
}
