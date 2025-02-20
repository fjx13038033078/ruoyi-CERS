package com.ruoyi.university.service.impl;

import com.ruoyi.university.domain.Major;
import com.ruoyi.university.mapper.MajorMapper;
import com.ruoyi.university.service.MajorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/2/20 17:25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {

    private final MajorMapper majorMapper;

    /**
     * 获取所有专业信息
     *
     * @return 专业列表
     */
    @Override
    public List<Major> getAllMajors() {
        return majorMapper.getAllMajors();
    }

    /**
     * 根据专业ID获取专业信息
     *
     * @param majorId 专业ID
     * @return 专业信息
     */
    @Override
    public Major getMajorById(Long majorId) {
        return majorMapper.getMajorById(majorId);
    }

    /**
     * 添加专业信息
     *
     * @param major 待添加的专业信息
     * @return 添加成功返回 true，否则返回 false
     */
    @Override
    public boolean addMajor(Major major) {
        int rows = majorMapper.addMajor(major);
        return rows > 0;
    }

    /**
     * 更新专业信息
     *
     * @param major 待更新的专业信息
     * @return 更新成功返回 true，否则返回 false
     */
    @Override
    public boolean updateMajor(Major major) {
        int rows = majorMapper.updateMajor(major);
        return rows > 0;
    }

    /**
     * 删除专业信息
     *
     * @param majorId 待删除的专业ID
     * @return 删除成功返回 true，否则返回 false
     */
    @Override
    public boolean deleteMajor(Long majorId) {
        int rows = majorMapper.deleteMajor(majorId);
        return rows > 0;
    }
}
