package com.ruoyi.university.mapper;

import com.ruoyi.university.domain.Major;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/2/20 17:14
 */
@Mapper
public interface MajorMapper {
    /**
     * 获取所有专业信息
     *
     * @return 专业信息列表
     */
    List<Major> getAllMajors();

    /**
     * 根据专业ID获取专业信息
     *
     * @param majorId 专业ID
     * @return 专业信息
     */
    Major getMajorById(Long majorId);

    /**
     * 根据学校ID获取该校所有专业信息
     *
     * @param universityId 学校ID
     * @return 专业信息列表
     */
    List<Major> getMajorsByUniversityId(Long universityId);

    /**
     * 根据多个专业ID查询专业信息
     *
     * @param majorIds 专业ID列表
     * @return 专业信息列表
     */
    List<Major> selectByIds(@Param("majorIds") List<Long> majorIds);

    /**
     * 添加专业信息
     *
     * @param major 待添加的专业信息
     * @return 添加成功返回影响的行数，否则返回0
     */
    int addMajor(Major major);

    /**
     * 更新专业信息
     *
     * @param major 待更新的专业信息
     * @return 更新成功返回影响的行数，否则返回0
     */
    int updateMajor(Major major);

    /**
     * 删除专业信息
     *
     * @param majorId 待删除的专业ID
     * @return 删除成功返回影响的行数，否则返回0
     */
    int deleteMajor(Long majorId);
}
