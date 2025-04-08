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

            fillUniversityName(recommendedMajors);
            return recommendedMajors;
        }else {
            return Collections.emptyList();
        }
    }

    private List<Major> getRecommendedMajorsByInfo(Long userId) {
        // 获取用户信息
        log.info("userId:{}", userId);
        Information userInfo = informationService.getInformationByUserId(userId);
        log.info("userInfo:{}", userInfo);
        if (userInfo == null) {
            return Collections.emptyList();
        }

        // 获取所有专业信息
        List<Major> allMajors = majorMapper.getAllMajors(null);

        // 获取所有高校信息，并转换为 Map<universityId, University>
        List<University> allUniversities = universityService.getAllUniversities(null);
        Map<Long, University> universityMap = allUniversities.stream()
                .collect(Collectors.toMap(University::getUniversityId, u -> u));

        // 调用推荐工具类
        log.info("userInfo:{}","allMajors:{}","universityMap:{}", userInfo,allMajors,universityMap);
        List<Major> majors = recommendMajors(userInfo, allMajors, universityMap);
        fillUniversityName(majors);
        return majors;
    }

    private void fillUniversityName(List<Major> majors) {
        for (Major major : majors) {
            Long universityId = major.getUniversityId();
            major.setUniversityName(universityService.getUniversityById(universityId).getUniversityName());
        }
    }

    private List<Major> recommendMajors(Information userInfo, List<Major> majorList, Map<Long, University> universityMap) {
        if (userInfo == null || majorList == null || majorList.isEmpty() || universityMap == null) {
            return Collections.emptyList();
        }

        // 过滤出符合用户学科类别（历史类/物理类）的专业
        List<Major> filteredBySubject = majorList.stream()
                .filter(major -> major.getSubject().equals(userInfo.getSubject()))
                .collect(Collectors.toList());
        log.info("filteredBySubject:{}", filteredBySubject);

        // 过滤符合用户目标高校层次（985/211/普通高校）
        List<Major> filteredByLevel = filteredBySubject.stream()
                .filter(major -> isUniversityLevelMatch(universityMap.get(major.getUniversityId()), userInfo.getUniversityLevel()))
                .collect(Collectors.toList());

        // 过滤符合用户高考成绩范围的专业（±30分）
        List<Major> recommendedMajors = filteredByLevel.stream()
                .filter(major -> Math.abs(major.getMinScore2024() - userInfo.getScore()) <= 30)
                .sorted(Comparator.comparing(Major::getMinScore2024)) // 按投档线排序
                .limit(3) // 限制最多返回 3 个
                .collect(Collectors.toList());

        return recommendedMajors;
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
