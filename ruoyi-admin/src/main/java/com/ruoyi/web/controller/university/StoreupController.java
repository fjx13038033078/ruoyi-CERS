package com.ruoyi.web.controller.university;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.university.domain.Storeup;
import com.ruoyi.university.service.StoreupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
     * 添加收藏
     *
     * @param storeup 待添加的收藏信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public AjaxResult addStoreup(@RequestBody Storeup storeup) {
        return toAjax(storeupService.addStoreup(storeup));
    }

    @PostMapping("/delete")
    public AjaxResult deleteStoreup(@RequestParam Long storeupId) {
        return toAjax(storeupService.deleteStoreup(storeupId));
    }
}
