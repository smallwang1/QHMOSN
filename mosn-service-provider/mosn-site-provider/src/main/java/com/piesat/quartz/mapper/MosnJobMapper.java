package com.piesat.quartz.mapper;

import com.piesat.quartz.entity.MosnJob;

import java.util.List;

/**
 * 调度任务信息 数据层
 */
public interface MosnJobMapper {

    /**
     * 查询调度任务日志集合
     * @param mosnJob 调度信息
     * @return 操作日志集合
     */
    public List<MosnJob> selectJobList(MosnJob mosnJob);

    /**
     * 查询所有调度任务
     * @return 度任务列表
     */
    public List<MosnJob> selectJobAll();

    /**
     * 过调度ID查询调度任务信息
     * @param jobId 调度ID
     * @return 角色对象信息
     */
    public MosnJob selectJobById(Long jobId);

    /**
     * 新增调度任务信息
     * @param mosnJob 调度任务信息
     * @return 结果
     */
    public int insertJob(MosnJob mosnJob);

    /**
     * 修改调度任务信息
     * @param mosnJob 调度任务信息
     * @return 结果
     */
    public int updateJob(MosnJob mosnJob);

    /**
     * 通过调度ID删除调度任务信息
     * @param jobId 调度ID
     * @return 结果
     */
    public int deleteJobById(Long jobId);

    /**
     * 批量删除调度任务信息
     * @param jobIds 需要删除的数据IDs
     * @return 结果
     */
    public int deleteJobByIds(Long[] jobIds);
}
