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
     * 根据目标用户，计算并推荐物品。
     *
     * @param targetUser 目标用户ID（Long类型）
     * @param numRecommendations 推荐物品的数量
     * @return 推荐的物品ID列表
     */
    public List<Long> recommendItems(Long targetUser, int numRecommendations) {
        // 存储目标用户与其他用户的相似度
        Map<Long, Double> userSimilarities = new HashMap<>();

        // 计算目标用户与其他所有用户的相似度
        for (Long user : userRatings.keySet()) {
            if (!user.equals(targetUser)) {
                double similarity = calculateSimilarity(targetUser, user);
                userSimilarities.put(user, similarity);
            }
        }

        // 根据相似度对用户进行排序，从高到低
        List<Map.Entry<Long, Double>> sortedSimilarities = new ArrayList<>(userSimilarities.entrySet());
        sortedSimilarities.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // 选择与目标用户最相似的若干个用户
        List<Long> similarUsers = new ArrayList<>();
        for (int i = 0; i < numRecommendations; i++) {
            if (i < sortedSimilarities.size()) {
                similarUsers.add(sortedSimilarities.get(i).getKey());
            } else {
                break;
            }
        }
        log.info("similarUsers:{}", similarUsers);

        // **提前获取 targetUser 的评分数据，防止 NullPointerException**
        Map<Long, Double> targetUserRatings = userRatings.get(targetUser);
        if (targetUserRatings == null) {
            targetUserRatings = new HashMap<>(); // 避免 NullPointerException
        }

        // 获取相似用户喜欢的物品，进行推荐
        Map<Long, Double> recommendations = new HashMap<>();
        for (Long user : similarUsers) {
            Map<Long, Double> ratings = userRatings.get(user);
            if (ratings == null) {
                continue; // 防止 NullPointerException
            }
            for (Long item : ratings.keySet()) {
                // **检查目标用户是否已经评价该物品**
                if (!targetUserRatings.containsKey(item)) {
                    recommendations.put(item, ratings.get(item));
                }
            }
        }

        // 按评分排序，取前N个推荐物品
        LinkedHashMap<Long, Double> sortedRecommendations = recommendations.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(numRecommendations)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return new ArrayList<>(sortedRecommendations.keySet());
    }
}
