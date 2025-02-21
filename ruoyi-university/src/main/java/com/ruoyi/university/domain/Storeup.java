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
     * 操作类型（1-收藏，2-浏览）
     */
    private Integer actionType;

    /**
     * 操作时间
     */
    private LocalDateTime actionTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
