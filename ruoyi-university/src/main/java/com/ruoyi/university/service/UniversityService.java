package com.ruoyi.university.service;

import com.ruoyi.university.domain.University;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/2/6 9:51
 */
public interface UniversityService {
    /**
     * 获取所有高校信息
     *
     * @return 高校列表
     */
    List<University> getAllUniversities(University university);

    /**
     * 根据高校ID获取高校信息
     *
     * @param universityId 高校ID
     * @return 高校信息
     */
    University getUniversityById(Long universityId);

    /**
     * 添加高校信息
     *
     * @param university 待添加的高校信息
     * @return 添加成功返回 true，否则返回 false
     */
    boolean addUniversity(University university);

    /**
     * 更新高校信息
     *
     * @param university 待更新的高校信息
     * @return 更新成功返回 true，否则返回 false
     */
    boolean updateUniversity(University university);

    /**
     * 删除高校信息
     *
     * @param universityId 待删除的高校ID
     * @return 删除成功返回 true，否则返回 false
     */
    boolean deleteUniversity(Long universityId);
}
