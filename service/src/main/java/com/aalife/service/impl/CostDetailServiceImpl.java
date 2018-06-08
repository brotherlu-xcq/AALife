package com.aalife.service.impl;

import com.aalife.bo.NewCostDetailBo;
import com.aalife.dao.entity.CostCategory;
import com.aalife.dao.entity.CostClean;
import com.aalife.dao.entity.CostDetail;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.CostCategoryRepository;
import com.aalife.dao.repository.CostCleanRepository;
import com.aalife.dao.repository.CostDetailRepository;
import com.aalife.dao.repository.CostGroupRepository;
import com.aalife.exception.BizException;
import com.aalife.service.CostDetailService;
import com.aalife.service.WebContext;
import com.aalife.utils.FormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * @author mosesc
 * @date 2018-06-08
 */
@Service
@Transactional
public class CostDetailServiceImpl implements CostDetailService {
    @Autowired
    private CostDetailRepository costDetailRepository;
    @Autowired
    private CostCategoryRepository costCategoryRepository;
    @Autowired
    private CostGroupRepository costGroupRepository;
    @Autowired
    private CostCleanRepository costCleanRepository;
    @Autowired
    private WebContext webContext;

    @Override
    public void createNewCostDetail(NewCostDetailBo costDetailBo) {
        User currentUser = webContext.getCurrentUser();
        String costDesc = costDetailBo.getCostDesc();
        CostCategory costCategory = costCategoryRepository.findOne(costDetailBo.getCateId());
        String costDateStr = costDetailBo.getCostDate();
        Date costDate = null;
        try {
            costDate = FormatUtil.parseString2Date(costDateStr, "yyyy-MM-dd");
        } catch (ParseException e) {
            throw new BizException("时间格式错误，请参照 yyyy-MM-dd");
        }
        if (costCategory == null){
            throw new BizException("未查询到对应得分类");
        }
        CostGroup costGroup = costGroupRepository.findGroupById(costDetailBo.getGroupId());
        if (costGroup == null){
            throw new BizException("未查询到账单");
        }
        CostDetail costDetail = new CostDetail();
        costDetail.setUser(currentUser);
        costDetail.setCostCategory(costCategory);
        costDetail.setCostDesc(costDesc);
        costDetail.setCostMoney(costDetailBo.getCostMoney());
        costDetail.setCostDate(costDate);
        costDetail.setEntryId(currentUser.getUserId());
        costDetail.setEntryDate(new Date());
        costDetail.setCostGroup(costGroup);
        costDetailRepository.save(costDetail);
    }

    @Override
    public void cleanCostDetail(Integer groupId, String comment) {
        // 创建结算记录
        User currentUser = webContext.getCurrentUser();
        CostClean costClean = new CostClean();
        costClean.setUser(currentUser);
        costClean.setComment(comment);
        costClean.setEntryId(currentUser.getUserId());
        costClean.setEntryDate(new Date());
        costCleanRepository.save(costClean);
        // 开始结算
        costDetailRepository.cleanCostDetailByGroup(groupId, costClean.getCleanId());
    }
}
