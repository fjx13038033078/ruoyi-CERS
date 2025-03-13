package com.ruoyi.university.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.university.domain.Information;
import com.ruoyi.university.mapper.InformationMapper;
import com.ruoyi.university.service.InformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/3 17:41
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InformationServiceImpl implements InformationService {
    private final InformationMapper informationMapper;

    private final ISysUserService iSysUserService;

    @Override
    public List<Information> getAllInformation() {
        List<Information> information = informationMapper.selectAll();
        fillInformation(information);
        return information;
    }

    @Override
    public Information getInformationById(Long infoId) {
        return informationMapper.selectById(infoId);
    }

    @Override
    public Information getInformationByUserId(Long userId) {
        return informationMapper.selectByUserId(userId);
    }

    @Override
    public boolean addInformation(Information information) {
        Long userId = SecurityUtils.getUserId();
        information.setUserId(userId);
        return informationMapper.insert(information) > 0;
    }

    @Override
    public boolean updateInformation(Information information) {
        return informationMapper.update(information) > 0;
    }

    @Override
    public boolean deleteInformation(Long infoId) {
        return informationMapper.deleteById(infoId) > 0;
    }

    //填充用户名
    private void fillInformation(List<Information> informations) {
        for (Information information : informations) {
            Long userId = information.getUserId();
            String nickName = iSysUserService.selectUserById(userId).getNickName();
            information.setUserName(nickName);
        }
    }
}
