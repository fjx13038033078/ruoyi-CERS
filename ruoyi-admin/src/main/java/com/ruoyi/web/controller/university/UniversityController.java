package com.ruoyi.web.controller.university;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.university.domain.University;
import com.ruoyi.university.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/2/6 14:19
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/university/university")
public class UniversityController extends BaseController {
    private final UniversityService universityService;

    /**
     * 获取所有高校列表
     * @return 高校列表
     */
    @GetMapping("/listAll")
    public TableDataInfo listAllUniversities(University university) {
        startPage();
        List<University> allUniversities = universityService.getAllUniversities(university);
        return getDataTable(allUniversities);
    }

    /**
     * 根据高校ID获取高校信息
     * @param universityId 高校ID
     * @return 高校信息
     */
    @GetMapping("/detail")
    public AjaxResult getUniversityById(@RequestParam Long universityId) {
        return success(universityService.getUniversityById(universityId));
    }

    /**
     * 添加高校信息
     * @param university 待添加的高校信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public AjaxResult addUniversity(@RequestBody University university) {
        return toAjax(universityService.addUniversity(university));
    }

    /**
     * 更新高校信息
     * @param university 待更新的高校信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public AjaxResult updateUniversity(@RequestBody University university) {
        return toAjax(universityService.updateUniversity(university));
    }

    /**
     * 删除高校信息
     * @param universityId 待删除的高校ID
     * @return 操作结果
     */
    @GetMapping("/delete")
    public AjaxResult deleteUniversity(@RequestParam Long universityId) {
        return toAjax(universityService.deleteUniversity(universityId));
    }
}
