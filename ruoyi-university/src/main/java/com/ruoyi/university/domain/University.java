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
 * @date 2025/2/6 9:44
 */
@TableName("cers_university")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class University implements Serializable {
    /**
     * 高校ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long universityId;

    /**
     * 高校名称
     */
    private String universityName;

    /**
     * 所在省份
     */
    private String location;

    /**
     * 高校排名
     */
    private Integer ranking;

    /**
     * 高校类型（如理工、综合、医学等）
     */
    private String type;

    /**
     * 是否985（0-否，1-是）
     */
    private Integer is985;

    /**
     * 是否211（0-否，1-是）
     */
    private Integer is211;

    /**
     * 2024年历史类最低投档线
     */
    private Integer minScoreLiberal;

    /**
     * 2024年物理类最低投档线
     */
    private Integer minScoreScience;

    @TableField(exist = false)
    private String keyword;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
