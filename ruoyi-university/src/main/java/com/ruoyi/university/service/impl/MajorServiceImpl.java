package com.ruoyi.university.service.impl;

import com.ruoyi.university.domain.Major;
import com.ruoyi.university.domain.University;
import com.ruoyi.university.mapper.MajorMapper;
import com.ruoyi.university.service.MajorService;
import com.ruoyi.university.service.UniversityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    private final UniversityService universityService;

    /**
     * 获取所有专业信息
     *
     * @return 专业列表
     */
    @Override
    public List<Major> getAllMajors(Major major) {
        List<Major> allMajors = majorMapper.getAllMajors(major);
        fillMajorInfo(allMajors);
        return allMajors;
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
     * 根据大学ID获取大学下的专业信息
     *
     * @param universityId 大学ID
     * @return 专业列表
     */
    @Override
    public List<Major> getMajorsByUniversityId(Long universityId) {
        return majorMapper.getMajorsByUniversityId(universityId);
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

    private void fillMajorInfo(List<Major> majors) {
        for (Major major : majors) {
            Long universityId = major.getUniversityId();
            if (universityId != null) {
                University university = universityService.getUniversityById(universityId);
                if (university != null) {
                    major.setUniversityName(university.getUniversityName());
                }
            }
        }
    }

}
