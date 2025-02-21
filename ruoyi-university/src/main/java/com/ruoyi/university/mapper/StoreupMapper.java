package com.ruoyi.university.mapper;

import com.ruoyi.university.domain.Storeup;
import org.apache.ibatis.annotations.Mapper;

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
}
