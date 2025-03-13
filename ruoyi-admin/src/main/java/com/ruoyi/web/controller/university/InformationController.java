package com.ruoyi.web.controller.university;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.university.domain.Information;
import com.ruoyi.university.service.InformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ruoyi.common.core.domain.AjaxResult.success;
import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * @Author 范佳兴
 * @date 2025/3/3 17:44
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/university/information")
public class InformationController extends BaseController {
    private final InformationService informationService;

    @GetMapping("/listAll")
    public TableDataInfo listAllInformation() {
        startPage();
        List<Information> allInformation = informationService.getAllInformation();
        return getDataTable(allInformation);
    }

    @GetMapping("/detail")
    public AjaxResult getInformationById(@RequestParam Long infoId) {
        return success(informationService.getInformationById(infoId));
    }

    @GetMapping("/listByUserId")
    public AjaxResult getInformationByUserId(@RequestParam Long userId) {
        return success(informationService.getInformationByUserId(userId));
    }

    @PostMapping("/add")
    public AjaxResult addInformation(@RequestBody Information information) {
        return success(informationService.addInformation(information));
    }

    @PostMapping("/update")
    public AjaxResult updateInformation(@RequestBody Information information) {
        return success(informationService.updateInformation(information));
    }

    @GetMapping("/delete")
    public AjaxResult deleteInformation(@RequestParam Long infoId) {
        return success(informationService.deleteInformation(infoId));
    }
}
