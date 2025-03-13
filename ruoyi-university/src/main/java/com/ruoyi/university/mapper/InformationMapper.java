package com.ruoyi.university.mapper;

import com.ruoyi.university.domain.Information;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/3 17:34
 */
@Mapper
public interface InformationMapper {

    /**
     * 查询所有用户高考信息
     *
     * @return 用户高考信息列表
     */
    List<Information> selectAll();

    /**
     * 根据ID查询用户高考信息
     *
     * @param infoId 用户高考信息ID
     * @return 用户高考信息
     */
    Information selectById(Long infoId);

    /**
     * 根据用户ID查询用户高考信息
     *
     * @param userId 用户ID
     * @return 用户高考信息
     */
    Information selectByUserId(Long userId);

    /**
     * 插入用户高考信息
     *
     * @param information 用户高考信息
     * @return 影响行数
     */
    int insert(Information information);

    /**
     * 更新用户高考信息
     *
     * @param information 用户高考信息
     * @return 影响行数
     */
    int update(Information information);

    /**
     * 删除用户高考信息
     *
     * @param infoId 用户高考信息ID
     * @return 影响行数
     */
    int deleteById(Long infoId);
}
