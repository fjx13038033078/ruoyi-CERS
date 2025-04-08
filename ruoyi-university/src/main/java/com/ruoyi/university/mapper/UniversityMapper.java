package com.ruoyi.university.mapper;

import com.ruoyi.university.domain.University;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/2/6 9:50
 */
@Mapper
public interface UniversityMapper {
    /**
     * 获取所有高校信息
     *
     * @return 高校信息列表
     */
    List<University> getAllUniversities(University university);

    /**
     * 根据高校ID获取高校信息
     *
     * @param universityId 高校ID
     * @return 高校信息
     */
    University getUniversityById(Long universityId);

    /**
     * 添加高校信息
     *
     * @param university 待添加的高校信息
     * @return 添加成功返回影响的行数，否则返回0
     */
    int addUniversity(University university);

    /**
     * 更新高校信息
     *
     * @param university 待更新的高校信息
     * @return 更新成功返回影响的行数，否则返回0
     */
    int updateUniversity(University university);

    /**
     * 删除高校信息
     *
     * @param universityId 待删除的高校ID
     * @return 删除成功返回影响的行数，否则返回0
     */
    int deleteUniversity(Long universityId);
}
