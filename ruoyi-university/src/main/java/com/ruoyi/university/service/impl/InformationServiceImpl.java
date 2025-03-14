package com.ruoyi.university.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.university.domain.Information;
import com.ruoyi.university.mapper.InformationMapper;
import com.ruoyi.university.service.InformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    private final ISysRoleService iSysRoleService;

    @Override
    public List<Information> getAllInformation() {
        Long userId = SecurityUtils.getUserId();
        String role = iSysRoleService.selectStringRoleByUserId(userId);
        if (role.equalsIgnoreCase("admin")) {
            List<Information> information = informationMapper.selectAll();
            fillInformation(information);
            return information;
        } else {
            Information information = informationMapper.selectByUserId(userId);
            List<Information> informations = new ArrayList<>();
            informations.add(information);
            fillInformation(informations);
            return informations;
        }
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
        Information information1 = informationMapper.selectByUserId(userId);
        if (information1 != null){
            throw new RuntimeException("您已填写过信息，请勿重复填写");
        }
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
            if (information != null) {
                Long userId = information.getUserId();
                String nickName = iSysUserService.selectUserById(userId).getNickName();
                information.setUserName(nickName);
            }
        }
    }
}
