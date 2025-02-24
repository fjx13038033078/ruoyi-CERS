package com.ruoyi.university.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author 范佳兴
 * @date 2025/2/21 10:00
 */
@TableName("cers_storeup")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Storeup implements Serializable {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long storeupId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 专业ID
     */
    private Long majorId;

    /**
     * 专业名称
     */
    @TableField(exist = false)
    private String majorName;

    /**
     * 学校名称
     */
    @TableField(exist = false)
    private String universityName;

    /**
     * 报名科目（1-文科，2-理科）
     */
    @TableField(exist = false)
    private Integer subject;

    /**
     * 2024年投档线
     */
    @TableField(exist = false)
    private Integer minScore2024;

    /**
     * 描述
     */
    @TableField(exist = false)
    private String description;

    /**
     * 操作类型（1-收藏，2-浏览）
     */
    private Integer actionType;

    /**
     * 操作时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime actionTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
