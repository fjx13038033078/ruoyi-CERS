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
     * 用户学科（1-历史类，2-物理类）
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

    /**
     * 高校类型（1-综合，2-工科，3-财经，4-农业，5-政法，6-医药，7-林业，8-师范，9-体育，10-语言，11-艺术，12-民族）
     */
    private Integer universityType;

    /**
     * 理想专业方向（1-工学，2-管理学，3-经济学，4-文学，5-医学，6-法学，7-教育学，8-农学，9-艺术学，10-哲学，11-历史学，12-理学）
     */
    private Integer idealMajorDirection;

    /**
     * 地域偏好（1-本省，2-一线城市，3-南方城市，4-北方城市，5-无要求）
     */
    private Integer regionPreference;

    /**
     * 日常兴趣（1-阅读写作，2-实验研究，3-编程技术，4-艺术创作，5-社交活动，6-户外活动，7-手工制作，8-商业实践）
     */
    private Integer dailyInterest;
}
