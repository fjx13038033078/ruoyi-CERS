package com.ruoyi.university.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.recommend.UserBasedCollaborativeFiltering;
import com.ruoyi.university.domain.Information;
import com.ruoyi.university.domain.Major;
import com.ruoyi.university.domain.University;
import com.ruoyi.university.mapper.MajorMapper;
import com.ruoyi.university.mapper.StoreupMapper;
import com.ruoyi.university.service.InformationService;
import com.ruoyi.university.service.MajorRecommendationService;
import com.ruoyi.university.service.UniversityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author 范佳兴
 * @date 2025/2/21 15:13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MajorRecommendationServiceImpl implements MajorRecommendationService {

    // 基于内容推荐的参数常量
    private static final int MAX_CONTENT_RECOMMENDATIONS = 8; // 基于内容推荐最大数量
    private static final int SCORE_RANGE_STRICT = 20; // 严格分数范围
    private static final int SCORE_RANGE_MODERATE = 40; // 中等分数范围
    private static final int SCORE_RANGE_LOOSE = 60; // 宽松分数范围

    private final MajorMapper majorMapper;

    private final StoreupMapper storeupMapper;

    private final UniversityService universityService;

    private final InformationService informationService;

    @Override
    public List<Major> getCombinedRecommendations() {
        // 获取协同过滤推荐结果
        List<Major> cfRecommendations = this.getCFRecommendations();

        // 获取基于用户画像的推荐结果
        Long userId = SecurityUtils.getUserId();
        List<Major> infoRecommendations = this.getRecommendedMajorsByInfo(userId);
        log.info("infoRecommendations:{}", infoRecommendations);

        // 合并结果并去重（根据专业ID去重）
        return Stream.concat(cfRecommendations.stream(), infoRecommendations.stream())
                .filter(Objects::nonNull) // 过滤空值
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                Major::getMajorId, // 以专业ID为唯一标识
                                major -> major,
                                (existing, replacement) -> existing // 重复时保留前者
                        ),
                        map -> new ArrayList<>(map.values())
                ));
    }

    @Override
    public List<Major> recommendMajors() {
        return getCombinedRecommendations();
    }

    private List<Major> getCFRecommendations() {

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

        // 创建协同过滤对象
        UserBasedCollaborativeFiltering filter = new UserBasedCollaborativeFiltering(userRatings);

        // 为目标用户推荐专业
        List<Long> recommendedMajorIds = filter.recommendItems(userId, 5);

        if (recommendedMajorIds!= null && !recommendedMajorIds.isEmpty()) {
            // 获取推荐的专业详情
            List<Major> recommendedMajors = majorMapper.selectByIds(recommendedMajorIds);

            // 获取高校信息用于批量填充高校名称
            List<University> allUniversities = universityService.getAllUniversities(null);
            Map<Long, University> universityMap = allUniversities.stream()
                    .collect(Collectors.toMap(University::getUniversityId, u -> u));
            
            fillUniversityNameBatch(recommendedMajors, universityMap);
            return recommendedMajors;
        }else {
            return Collections.emptyList();
        }
    }

    private List<Major> getRecommendedMajorsByInfo(Long userId) {
        // 获取用户信息
        log.info("开始为用户 {} 生成基于画像的推荐", userId);
        Information userInfo = informationService.getInformationByUserId(userId);
        if (userInfo == null) {
            log.warn("用户 {} 没有完善个人信息，无法进行基于画像的推荐", userId);
            return Collections.emptyList();
        }

        // 获取所有专业信息
        List<Major> allMajors = majorMapper.getAllMajors(null);
        if (allMajors == null || allMajors.isEmpty()) {
            log.warn("系统中没有专业数据");
            return Collections.emptyList();
        }

        // 获取所有高校信息，并转换为 Map<universityId, University>
        List<University> allUniversities = universityService.getAllUniversities(null);
        Map<Long, University> universityMap = allUniversities.stream()
                .collect(Collectors.toMap(University::getUniversityId, u -> u));

        // 调用推荐工具类
        List<Major> majors = recommendMajorsOptimized(userInfo, allMajors, universityMap);
        
        // 优化后的批量填充高校名称
        fillUniversityNameBatch(majors, universityMap);
        
        log.info("为用户 {} 基于画像推荐了 {} 个专业", userId, majors.size());
        return majors;
    }

    /**
     * 优化后的批量填充高校名称，避免N+1查询问题
     */
    private void fillUniversityNameBatch(List<Major> majors, Map<Long, University> universityMap) {
        for (Major major : majors) {
            Long universityId = major.getUniversityId();
            University university = universityMap.get(universityId);
            if (university != null) {
                major.setUniversityName(university.getUniversityName());
            } else {
                log.warn("专业 {} 关联的高校 {} 不存在", major.getMajorId(), universityId);
                major.setUniversityName("未知高校");
            }
        }
    }

    /**
     * 优化后的专业推荐方法
     */
    private List<Major> recommendMajorsOptimized(Information userInfo, List<Major> majorList, Map<Long, University> universityMap) {
        if (userInfo == null || majorList == null || majorList.isEmpty() || universityMap == null) {
            return Collections.emptyList();
        }

        log.info("用户画像信息 - 学科类别: {}, 高校层次: {}, 分数: {}", 
                userInfo.getSubject(), userInfo.getUniversityLevel(), userInfo.getScore());

        // 1. 过滤出符合用户学科类别的专业
        List<Major> filteredBySubject = majorList.stream()
                .filter(major -> major.getSubject().equals(userInfo.getSubject()))
                .collect(Collectors.toList());
        
        log.info("按学科类别过滤后剩余 {} 个专业", filteredBySubject.size());

        // 2. 过滤符合用户目标高校层次的专业
        List<Major> filteredByLevel = filteredBySubject.stream()
                .filter(major -> isUniversityLevelMatch(universityMap.get(major.getUniversityId()), userInfo.getUniversityLevel()))
                .collect(Collectors.toList());

        log.info("按高校层次过滤后剩余 {} 个专业", filteredByLevel.size());

        // 3. 分层推荐：冲刺、匹配、保底
        List<Major> recommendedMajors = new ArrayList<>();
        
        // 冲刺院校（分数高于用户20-40分的专业）
        List<Major> reachMajors = getScoreRangeMajors(filteredByLevel, userInfo.getScore(), 20, 40, 2);
        recommendedMajors.addAll(reachMajors);
        
        // 匹配院校（分数±20分范围内的专业）
        List<Major> matchMajors = getScoreRangeMajors(filteredByLevel, userInfo.getScore(), -SCORE_RANGE_STRICT, SCORE_RANGE_STRICT, 4);
        recommendedMajors.addAll(matchMajors);
        
        // 保底院校（分数低于用户20-60分的专业）
        List<Major> safeMajors = getScoreRangeMajors(filteredByLevel, userInfo.getScore(), -SCORE_RANGE_LOOSE, -SCORE_RANGE_STRICT, 2);
        recommendedMajors.addAll(safeMajors);

        // 去重并限制总数量
        List<Major> finalRecommendations = recommendedMajors.stream()
                .distinct() // 去重
                .limit(MAX_CONTENT_RECOMMENDATIONS)
                .collect(Collectors.toList());

        log.info("最终推荐专业数量: {} (冲刺: {}, 匹配: {}, 保底: {})", 
                finalRecommendations.size(), reachMajors.size(), matchMajors.size(), safeMajors.size());

        return finalRecommendations;
    }

    /**
     * 根据分数范围获取专业，并进行智能排序
     */
    private List<Major> getScoreRangeMajors(List<Major> majors, Integer userScore, int minOffset, int maxOffset, int limit) {
        return majors.stream()
                .filter(major -> {
                    int scoreDiff = major.getMinScore2024() - userScore;
                    return scoreDiff >= minOffset && scoreDiff <= maxOffset;
                })
                .sorted((m1, m2) -> {
                    // 多维度排序：优先考虑分数接近度，然后考虑专业热度等
                    int scoreDiff1 = Math.abs(m1.getMinScore2024() - userScore);
                    int scoreDiff2 = Math.abs(m2.getMinScore2024() - userScore);
                    
                    // 首先按分数接近度排序
                    int scoreComparison = Integer.compare(scoreDiff1, scoreDiff2);
                    if (scoreComparison != 0) {
                        return scoreComparison;
                    }
                    
                    // 分数相同时，可以按其他因素排序（这里简化为按专业ID）
                    return Long.compare(m1.getMajorId(), m2.getMajorId());
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 判断专业所属高校是否符合用户的目标高校层次
     *
     * @param university 高校对象
     * @param targetLevel 用户目标高校层次（0-985, 1-211, 2-普通高校）
     * @return 是否匹配
     */
    private static boolean isUniversityLevelMatch(University university, Integer targetLevel) {
        if (university == null) {
            return false;
        }

        if (targetLevel == 0) {
            return university.getIs985() == 1; // 985高校
        } else if (targetLevel == 1) {
            return university.getIs211() == 1 || university.getIs985() == 1; // 211高校（包含985）
        } else {
            return university.getIs985() == 0 && university.getIs211() == 0; // 普通高校
        }
    }
}
