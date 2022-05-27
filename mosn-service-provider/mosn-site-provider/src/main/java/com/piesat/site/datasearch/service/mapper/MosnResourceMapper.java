package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnResource;

import java.util.List;

public interface MosnResourceMapper {

    /**
     * 查询产品资料集合
     * @param resource
     * @return
     */
    List<MosnResource> selectResourceList(MosnResource resource);

    /**
     * 根据Id查询资料
     * @param resourceId
     * @return
     */
    MosnResource selectResourceById(Long resourceId);

    /**
     * 根据IDs查询资料集合
     * @param resouceIds
     * @return
     */
    List<MosnResource> selectResourceByIds(Long[] resouceIds);

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
