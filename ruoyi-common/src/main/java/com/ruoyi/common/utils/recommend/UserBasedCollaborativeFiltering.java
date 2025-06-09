package com.ruoyi.common.utils.recommend;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 范佳兴
 * @date 2025/2/12 16:55
 */
@Slf4j
public class UserBasedCollaborativeFiltering {
    // 推荐算法优化常量
    private static final double SIMILARITY_THRESHOLD = 0.1; // 相似度阈值，过滤掉相似度过低的用户
    private static final int MAX_RECOMMENDATIONS = 10; // 最大推荐数量
    private static final int MAX_SIMILAR_USERS = 20; // 最大相似用户数量，减少计算量

    // 用户评分数据，Map存储用户对物品的评分，Map<用户ID(Long), Map<物品ID(Long), 评分(Double)>>
    private Map<Long, Map<Long, Double>> userRatings;

    // 物品对应的用户列表，Map存储每个物品被哪些用户评分，Map<物品ID(Long), List<用户ID(Long)>>
    private Map<Long, List<Long>> itemUsers;

    // 用户到索引的映射，Map<用户ID(Long), 索引(Integer)>
    private Map<Long, Integer> userIndex;

    // 索引到用户的映射，Map<索引(Integer), 用户ID(Long)>
    private Map<Integer, Long> indexUser;

    // 用户相似度矩阵，用于存储用户之间的相似度
    private Long[][] sparseMatrix;

    /**
     * 构造函数，初始化工具类。
     *
     * @param userRatings 用户评分数据，Map<用户ID(Long), Map<物品ID(Long), 评分(Double)>>
     */
    public UserBasedCollaborativeFiltering(Map<Long, Map<Long, Double>> userRatings) {
        this.userRatings = userRatings;
        this.itemUsers = new HashMap<>();

        // 初始化用户索引映射和索引到用户的反向映射
        this.userIndex = new HashMap<>();
        this.indexUser = new HashMap<>();

        // 构建物品-用户倒排表：记录每个物品被哪些用户评分
        int keyIndex = 0;
        for (Long user : userRatings.keySet()) {
            Map<Long, Double> ratings = userRatings.get(user);
            for (Long item : ratings.keySet()) {
                itemUsers.computeIfAbsent(item, k -> new ArrayList<>()).add(user);
            }
            this.userIndex.put(user, keyIndex);
            this.indexUser.put(keyIndex, user);
            keyIndex++;
        }

        // 构建用户相似度矩阵
        int N = userRatings.size();
        this.sparseMatrix = new Long[N][N]; // 初始化用户相似度矩阵
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.sparseMatrix[i][j] = 0L;
            }
        }

        // 根据物品和用户的倒排表构建用户之间的相似度矩阵
        for (Long item : itemUsers.keySet()) {
            List<Long> userList = itemUsers.get(item);
            for (Long u1 : userList) {
                for (Long u2 : userList) {
                    if (!u1.equals(u2)) {
                        int id1 = this.userIndex.get(u1);
                        int id2 = this.userIndex.get(u2);
                        this.sparseMatrix[id1][id2] += 1; // 用户u1和u2对物品item有共同评分
                    }
                }
            }
        }
    }

    /**
     * 计算两个用户之间的相似度（使用余弦相似度公式）。
     *
     * @param user1 第一个用户ID（Long类型）
     * @param user2 第二个用户ID（Long类型）
     * @return 用户之间的相似度值
     */
    public double calculateSimilarity(Long user1, Long user2) {
        // 获取用户对应的索引
        Integer id1 = this.userIndex.get(user1);
        Integer id2 = this.userIndex.get(user2);

        // 如果用户不存在返回相似度为0
        if (id1 == null || id2 == null) {
            return 0.0;
        }

        // 计算余弦相似度
        return this.sparseMatrix[id1][id2] / Math.sqrt(userRatings.get(indexUser.get(id1)).size() * userRatings.get(indexUser.get(id2)).size());
    }

    /**
     * 根据目标用户，计算并推荐物品（优化版本）。
     *
     * @param targetUser 目标用户ID（Long类型）
     * @param numRecommendations 推荐物品的数量（最大为10）
     * @return 推荐的物品ID列表
     */
    public List<Long> recommendItems(Long targetUser, int numRecommendations) {
        // 限制推荐数量不超过10
        numRecommendations = Math.min(numRecommendations, MAX_RECOMMENDATIONS);
        
        // 提前获取目标用户的评分数据，防止 NullPointerException
        Map<Long, Double> targetUserRatings = userRatings.get(targetUser);
        if (targetUserRatings == null || targetUserRatings.isEmpty()) {
            log.warn("目标用户 {} 没有评分数据，无法进行推荐", targetUser);
            return new ArrayList<>();
        }

        // 存储目标用户与其他用户的相似度（只存储超过阈值的）
        Map<Long, Double> userSimilarities = new HashMap<>();

        // 计算目标用户与其他所有用户的相似度（优化：添加相似度阈值过滤）
        for (Long user : userRatings.keySet()) {
            if (!user.equals(targetUser)) {
                double similarity = calculateSimilarity(targetUser, user);
                // 只保留相似度超过阈值的用户
                if (similarity > SIMILARITY_THRESHOLD) {
                    userSimilarities.put(user, similarity);
                }
            }
        }

        if (userSimilarities.isEmpty()) {
            log.warn("没有找到与用户 {} 相似度超过阈值的用户", targetUser);
            return new ArrayList<>();
        }

        // 根据相似度对用户进行排序，从高到低，并限制相似用户数量
        List<Long> similarUsers = userSimilarities.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(MAX_SIMILAR_USERS) // 限制相似用户数量，提高效率
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        log.info("找到 {} 个相似用户", similarUsers.size());

        // 获取相似用户喜欢的物品，进行推荐（优化：使用加权评分）
        Map<Long, Double> recommendations = new HashMap<>();
        for (Long user : similarUsers) {
            Map<Long, Double> ratings = userRatings.get(user);
            if (ratings == null) {
                continue;
            }
            
            double userSimilarity = userSimilarities.get(user);
            for (Map.Entry<Long, Double> entry : ratings.entrySet()) {
                Long item = entry.getKey();
                Double rating = entry.getValue();
                
                // 检查目标用户是否已经评价该物品
                if (!targetUserRatings.containsKey(item)) {
                    // 使用相似度加权评分
                    Double weightedRating = rating * userSimilarity;
                    recommendations.merge(item, weightedRating, Double::sum);
                }
            }
        }

        if (recommendations.isEmpty()) {
            log.warn("没有找到可推荐的物品给用户 {}", targetUser);
            return new ArrayList<>();
        }

        // 按加权评分排序，取前N个推荐物品
        List<Long> result = recommendations.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(numRecommendations)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        log.info("为用户 {} 推荐了 {} 个物品", targetUser, result.size());
        return result;
    }
    
    /**
     * 获取默认推荐（限制为10个）
     */
    public List<Long> recommendItems(Long targetUser) {
        return recommendItems(targetUser, MAX_RECOMMENDATIONS);
    }
}

