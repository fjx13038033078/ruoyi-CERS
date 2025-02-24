package com.ruoyi.university.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.university.domain.Storeup;
import com.ruoyi.university.mapper.StoreupMapper;
import com.ruoyi.university.service.MajorService;
import com.ruoyi.university.service.StoreupService;
import com.ruoyi.university.service.UniversityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * @Author 范佳兴
 * @date 2025/2/21 11:03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StoreupServiceImpl implements StoreupService {

    private final StoreupMapper storeupMapper;

    private final MajorService majorService;

    private final UniversityService universityService;

    /**
     * 添加收藏
     *
     * @param storeup 待添加的收藏信息
     * @return 添加成功返回 true，否则返回 false
     */
    @Override
    public boolean addStoreup(Storeup storeup) {
        Long userId = SecurityUtils.getUserId();
        if (storeup != null) {
            List<Storeup> storeups = storeupMapper.selectStoreupByUserIdAndMajorId(userId, storeup.getMajorId());
            if (storeups.size() > 0) {
                throw new RuntimeException("该收藏已存在,无需重复收藏");
            }
        }
        storeup.setUserId(userId);
        int rows = storeupMapper.addStoreup(storeup);
        return rows > 0;
    }

    /**
     * 删除收藏
     *
     * @param storeupId 待删除的收藏信息 ID
     * @return 删除成功返回 true，否则返回 false
     */
    @Override
    public boolean deleteStoreup(Long storeupId) {
        int rows = storeupMapper.deleteStoreup(storeupId);
        return rows > 0;
    }

    /**
     * 查询所有收藏
     *
     * @return 收藏列表
     */
    @Override
    public List<Storeup> selectAllStoreup() {
        Long userId = SecurityUtils.getUserId();
        startPage();
        if (userId == 1) {
            List<Storeup> storeups = storeupMapper.selectAllStoreup();
            fillStoreup(storeups);
            return storeups;
        } else {
            List<Storeup> storeups = storeupMapper.selectStoreupByUserId(userId);
            fillStoreup(storeups);
            return storeups;
        }
    }

    private void fillStoreup(List<Storeup> storeups) {
        for (Storeup storeup : storeups) {
            Long majorId = storeup.getMajorId();
            storeup.setMajorName(majorService.getMajorById(majorId).getMajorName());
            storeup.setDescription(majorService.getMajorById(majorId).getDescription());
            storeup.setSubject(majorService.getMajorById(majorId).getSubject());
            storeup.setMinScore2024(majorService.getMajorById(majorId).getMinScore2024());
            storeup.setUniversityName(universityService.getUniversityById(majorService.getMajorById(majorId).getUniversityId()).getUniversityName());
        }
    }
}
