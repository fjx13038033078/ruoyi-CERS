package com.ruoyi.university.service;

import com.ruoyi.university.domain.Storeup;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/2/21 11:03
 */
public interface StoreupService {
    /**
     * 添加收藏
     *
     * @param storeup 待添加的收藏信息
     * @return 添加成功返回 true，否则返回 false
     */
    boolean addStoreup(Storeup storeup);

    /**
     * 删除收藏
     *
     * @param storeupId 待删除的收藏信息 ID
     * @return 删除成功返回 true，否则返回 false
     */
    boolean deleteStoreup(Long storeupId);

    /**
     * 查询所有收藏
     *
     * @return 所有收藏信息列表
     */
    List<Storeup> selectAllStoreup();
}
