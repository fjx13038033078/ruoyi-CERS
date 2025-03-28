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
 * @date 2025/2/5 10:28
 */
@TableName("cers_province")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Province implements Serializable {
    /**
     * 省份ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long provinceId;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 总高校数量
     */
    private Integer numTotal;

    /**
     * 985高校数量
     */
    private Integer num985;

    /**
     * 211高校数量
     */
    private Integer num211;

    /**
     * 2024年文科最低投档线
     */
    private Integer minScoreLiberal;

    /**
     * 2024年理科最低投档线
     */
    private Integer minScoreScience;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
