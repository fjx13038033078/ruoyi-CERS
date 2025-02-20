package com.ruoyi.university.service;

import com.ruoyi.university.domain.Major;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/2/20 17:24
 */
public interface MajorService {
    /**
     * 获取所有专业信息
     *
     * @return 专业列表
     */
    List<Major> getAllMajors();

    /**
     * 根据专业ID获取专业信息
     *
     * @param majorId 专业ID
     * @return 专业信息
     */
    Major getMajorById(Long majorId);

    /**
     * 添加专业信息
     *
     * @param major 待添加的专业信息
     * @return 添加成功返回 true，否则返回 false
     */
    boolean addMajor(Major major);

    /**
     * 更新专业信息
     *
     * @param major 待更新的专业信息
     * @return 更新成功返回 true，否则返回 false
     */
    boolean updateMajor(Major major);

    /**
     * 删除专业信息
     *
     * @param majorId 待删除的专业ID
     * @return 删除成功返回 true，否则返回 false
     */
    boolean deleteMajor(Long majorId);
}
