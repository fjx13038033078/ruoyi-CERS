package com.ruoyi.university.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.recommend.UserBasedCollaborativeFiltering;
import com.ruoyi.university.domain.Major;
import com.ruoyi.university.mapper.MajorMapper;
import com.ruoyi.university.mapper.StoreupMapper;
import com.ruoyi.university.service.MajorRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 范佳兴
 * @date 2025/2/21 15:13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MajorRecommendationServiceImpl implements MajorRecommendationService {

    private final MajorMapper majorMapper;

    private final StoreupMapper storeupMapper;

    @Override
    public List<Major> recommendMajors() {

        Long userId = SecurityUtils.getUserId();
        // 获取所有用户的行为记录（收藏、浏览），按userId, majorId, actionType过滤
        List<Map<String, Object>> storeupRecords = storeupMapper.selectUserActions(null, null, null);
        log.info("storeupRecords:{}", storeupRecords);

        // 构建用户评分数据，Map<用户ID, Map<专业ID, 评分>>
        Map<Long, Map<Long, Double>> userRatings = new HashMap<>();
        for (Map<String, Object> record : storeupRecords) {
            Long user = (Long) record.get("user_id");
            Long major = (Long) record.get("major_id");
            Integer actionType = (Integer) record.get("action_type");
            Double rating = actionType == 1 ? 1.0 : 0.1; // 收藏赋予1分，浏览赋予0.5分

            // 为用户添加评分记录
            userRatings.computeIfAbsent(user, k -> new HashMap<>()).put(major, rating);
        }
        log.info("userRatings:{}", userRatings);

        // 创建协同过滤对象
        UserBasedCollaborativeFiltering filter = new UserBasedCollaborativeFiltering(userRatings);

        // 为目标用户推荐专业
        List<Long> recommendedMajorIds = filter.recommendItems(userId, 5);
        log.info("recommendedMajorIds:{}", recommendedMajorIds);

        if (recommendedMajorIds!= null && !recommendedMajorIds.isEmpty()) {
            // 获取推荐的专业详情
            List<Major> recommendedMajors = majorMapper.selectByIds(recommendedMajorIds);
            log.info("recommendedMajors:{}", recommendedMajors);
            return recommendedMajors;
        }else {
            return Collections.emptyList();
        }
    }
}
