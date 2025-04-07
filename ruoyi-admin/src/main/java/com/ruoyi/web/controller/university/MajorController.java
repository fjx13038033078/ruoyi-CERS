package com.ruoyi.web.controller.university;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.university.domain.Major;
import com.ruoyi.university.service.MajorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/2/20 17:25
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/university/major")
@Slf4j
public class MajorController extends BaseController {

    private final MajorService majorService;

    /**
     * 获取所有专业列表
     * @return 专业列表
     */
    @GetMapping("/listAll")
    public TableDataInfo listAllMajors(Major major) {
        log.info("获取所有专业列表",major);
        startPage();
        List<Major> allMajors = majorService.getAllMajors(major);
        return getDataTable(allMajors);
    }

    /**
     * 根据专业ID获取专业信息
     * @param majorId 专业ID
     * @return 专业信息
     */
    @GetMapping("/detail")
    public AjaxResult getMajorById(@RequestParam Long majorId) {
        return success(majorService.getMajorById(majorId));
    }

    /**
     * 根据大学ID获取大学下的所有专业
     * @param universityId 大学ID
     * @return 专业列表
     */
    @GetMapping("/listByUniversityId")
    public AjaxResult getMajorsByUniversityId(@RequestParam Long universityId) {
        return success(majorService.getMajorsByUniversityId(universityId));
    }

    /**
     * 添加专业信息
     * @param major 待添加的专业信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public AjaxResult addMajor(@RequestBody Major major) {
        return toAjax(majorService.addMajor(major));
    }

    /**
     * 更新专业信息
     * @param major 待更新的专业信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public AjaxResult updateMajor(@RequestBody Major major) {
        return toAjax(majorService.updateMajor(major));
    }

    /**
     * 删除专业信息
     * @param majorId 待删除的专业ID
     * @return 操作结果
     */
    @GetMapping("/delete")
    public AjaxResult deleteMajor(@RequestParam Long majorId) {
        return toAjax(majorService.deleteMajor(majorId));
    }
}
