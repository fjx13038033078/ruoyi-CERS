package com.ruoyi.university.service;

import com.ruoyi.university.domain.Major;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/2/21 15:13
 */
public interface MajorRecommendationService {
    /**
     * 根据用户行为记录，生成推荐的专业列表
     *
     * @return 推荐的专业列表
     */
    List<Major> recommendMajors();
}
