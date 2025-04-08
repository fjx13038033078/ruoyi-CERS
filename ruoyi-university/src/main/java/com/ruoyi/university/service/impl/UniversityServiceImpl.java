package com.ruoyi.university.service.impl;

import com.ruoyi.university.domain.University;
import com.ruoyi.university.service.UniversityService;
import com.ruoyi.university.mapper.UniversityMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/2/6 9:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityMapper universityMapper;

    /**
     * 获取所有高校信息
     *
     * @return 高校列表
     */
    @Override
    public List<University> getAllUniversities(University university) {
        return universityMapper.getAllUniversities(university);
    }

    /**
     * 根据高校ID获取高校信息
     *
     * @param universityId 高校ID
     * @return 高校信息
     */
    @Override
    public University getUniversityById(Long universityId) {
        return universityMapper.getUniversityById(universityId);
    }

    /**
     * 添加高校信息
     *
     * @param university 待添加的高校信息
     * @return 添加成功返回 true，否则返回 false
     */
    @Override
    public boolean addUniversity(University university) {
        int rows = universityMapper.addUniversity(university);
        return rows > 0;
    }

    /**
     * 更新高校信息
     *
     * @param university 待更新的高校信息
     * @return 更新成功返回 true，否则返回 false
     */
    @Override
    public boolean updateUniversity(University university) {
        int rows = universityMapper.updateUniversity(university);
        return rows > 0;
    }

    /**
     * 删除高校信息
     *
     * @param universityId 待删除的高校ID
     * @return 删除成功返回 true，否则返回 false
     */
    @Override
    public boolean deleteUniversity(Long universityId) {
        int rows = universityMapper.deleteUniversity(universityId);
        return rows > 0;
    }
}
