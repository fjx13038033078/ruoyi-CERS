package com.ruoyi.university.mapper;

import com.ruoyi.university.domain.Storeup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author 范佳兴
 * @date 2025/2/21 10:06
 */
@Mapper
public interface StoreupMapper {

    /**
     * 添加收藏
     * @param storeup
     * @return
     */
    int addStoreup(Storeup storeup);

    /**
     * 删除收藏
     * @param storeupId
     * @return
     */
    int deleteStoreup(Long storeupId);

    /**
     * 获取所有用户的行为记录（收藏、浏览），按userId, majorId, actionType过滤
     * @param userId 用户ID
     * @param majorId 专业ID
     * @param actionType 操作类型（收藏/浏览）
     * @return 行为记录列表
     */
    List<Map<String, Object>> selectUserActions(@Param("userId") Long userId,
                                                @Param("majorId") Long majorId,
                                                @Param("actionType") Integer actionType);
}
