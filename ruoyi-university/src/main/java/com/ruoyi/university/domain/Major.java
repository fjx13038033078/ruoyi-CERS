package com.ruoyi.university.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author 范佳兴
 * @date 2025/2/20 17:13
 */
@TableName("cers_major")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Major implements Serializable {
    /**
     * 专业ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long majorId;

    /**
     * 学校ID，外键关联到高校表
     */
    private Long universityId;

    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 报名科目（1-文科，2-理科）
     */
    private Integer subject;

    /**
     * 2024年投档线
     */
    private Integer minScore2024;

    /**
     * 专业简介
     */
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
