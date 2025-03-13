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
 * @date 2025/3/3 17:33
 */
@TableName("cers_information")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Information implements Serializable {
    /**
     * 主键ID，自增
     */
    @TableId(type = IdType.AUTO)
    private Long infoId;

    /**
     * 用户ID，外键
     */
    private Long userId;

    /**
     * 用户姓名
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 用户学科（1-文科，2-理科）
     */
    private Integer subject;

    /**
     * 目标高校水平(0-985,1-211,2-普通高校)
     */
    private Integer universityLevel;

    /**
     * 高考成绩
     */
    private Integer score;
}
