package com.ruoyi.web.controller.university;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.university.domain.Major;
import com.ruoyi.university.domain.Storeup;
import com.ruoyi.university.service.StoreupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/2/21 11:05
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/university/storeup")
public class StoreupController extends BaseController {

    private final StoreupService storeupService;

    /**
     * 获取所有收藏列表
     * @return 收藏列表
     */
    @GetMapping("/listAll")
    public TableDataInfo listAllMajors() {
        List<Storeup> storeups = storeupService.selectAllStoreup();
        return getDataTable(storeups);
    }

    /**
     * 添加收藏
     *
     * @param storeup 待添加的收藏信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public AjaxResult addStoreup(@RequestBody Storeup storeup) {
        return toAjax(storeupService.addStoreup(storeup));
    }

    /**
     * 删除收藏
     *
     * @param storeupId 收藏id
     * @return 操作结果
     */
    @GetMapping("/delete")
    public AjaxResult deleteStoreup(@RequestParam Long storeupId) {
        return toAjax(storeupService.deleteStoreup(storeupId));
    }
}
