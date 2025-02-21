package com.ruoyi.web.controller.university;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.university.domain.Major;
import com.ruoyi.university.domain.Province;
import com.ruoyi.university.service.MajorRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * @Author 范佳兴
 * @date 2025/2/21 15:48
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/university/recommendation")
public class MajorRecommendationController extends BaseController {

    private final MajorRecommendationService majorRecommendationService;


    /**
     * 获取推荐专业列表
     * @return
     */
    @GetMapping("/listAll")
    public AjaxResult recommendMajors() {
        List<Major> majors = majorRecommendationService.recommendMajors();
        return success(majors);
    }
}
