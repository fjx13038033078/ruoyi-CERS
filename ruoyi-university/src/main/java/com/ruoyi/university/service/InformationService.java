package com.ruoyi.university.service;

import com.ruoyi.university.domain.Information;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/3 17:40
 */
public interface InformationService {

    /**
     * 获取所有用户高考信息
     *
     * @return 用户高考信息列表
     */
    List<Information> getAllInformation();

    /**
     * 根据ID获取用户高考信息
     *
     * @param infoId 用户高考信息ID
     * @return 用户高考信息
     */
    Information getInformationById(Long infoId);

    /**
     * 根据用户ID获取用户高考信息
     *
     * @param userId 用户ID
     * @return 用户高考信息
     */
    Information getInformationByUserId(Long userId);

    /**
     * 添加用户高考信息
     *
     * @param information 用户高考信息
     * @return 是否成功
     */
    boolean addInformation(Information information);

    /**
     * 更新用户高考信息
     *
     * @param information 用户高考信息
     * @return 是否成功
     */
    boolean updateInformation(Information information);

    /**
     * 删除用户高考信息
     *
     * @param infoId 用户高考信息ID
     * @return 是否成功
     */
    boolean deleteInformation(Long infoId);
}
