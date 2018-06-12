package com.aalife.service.impl;

import com.aalife.bo.BaseQueryBo;
import com.aalife.bo.BaseQueryResultBo;
import com.aalife.bo.CostCategoryBo;
import com.aalife.bo.CostDetailBo;
import com.aalife.bo.CostGroupBo;
import com.aalife.bo.ExtendUserBo;
import com.aalife.bo.NewCostDetailBo;
import com.aalife.bo.WxQueryBo;
import com.aalife.bo.WxQueryCriteriaBo;
import com.aalife.dao.entity.CostCategory;
import com.aalife.dao.entity.CostClean;
import com.aalife.dao.entity.CostDetail;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.entity.CostGroupUser;
import com.aalife.dao.entity.CostUserRemark;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.CostCategoryRepository;
import com.aalife.dao.repository.CostCleanRepository;
import com.aalife.dao.repository.CostDetailRepository;
import com.aalife.dao.repository.CostGroupRepository;
import com.aalife.dao.repository.CostGroupUserRepository;
import com.aalife.dao.repository.CostUserRemarkRepository;
import com.aalife.exception.BizException;
import com.aalife.service.CostDetailService;
import com.aalife.service.WebContext;
import com.aalife.utils.FormatUtil;
import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private CostUserRemarkRepository costUserRemarkRepository;
    @Autowired
    private CostGroupUserRepository costGroupUserRepository;
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

    @Override
    public BaseQueryResultBo<CostDetailBo> listUncleanCostDetailByGroup(WxQueryBo wxQueryBo) {
        PageRequest pageRequest = new PageRequest(wxQueryBo.getPage() -1, wxQueryBo.getSize());
        Specification<CostDetail> specification = new CostDetailSpecification(wxQueryBo);
        Page<CostDetail> costDetailPage = costDetailRepository.findAll(specification, pageRequest);
        List<CostDetail> costDetails = costDetailPage.getContent();
        List<CostDetailBo> costDetailBos = new ArrayList<>();
        // 用户存放不至于每次重复查
        Map<String, String> remarkNames = new HashMap<>(8);
        User currentUser = webContext.getCurrentUser();
        if (costDetails != null){
            for (CostDetail costDetail : costDetails){
                CostDetailBo costDetailBo = new CostDetailBo();
                // 设置用户信息
                User user = costDetail.getUser();
                ExtendUserBo userBo = new ExtendUserBo();
                userBo.setAvatarUrl(user.getAvatarUrl());
                userBo.setNickName(user.getNickName());
                userBo.setUserId(user.getUserId());
                String key = currentUser.getUserId() + "-"+user.getUserId();
                String remarkName = remarkNames.get(key);
                if (remarkName == null){
                    CostUserRemark costUserRemark = costUserRemarkRepository.findRemarkBySourceAndTarget(currentUser.getUserId(), user.getUserId());
                    remarkName = costUserRemark == null ? user.getNickName() : costUserRemark.getRemarkName();
                    remarkNames.put(key, remarkName);
                }
                userBo.setRemarkName(remarkName);
                // 设置基本消费信息
                costDetailBo.setUser(userBo);
                costDetailBo.setCostMoney(costDetail.getCostMoney());
                costDetailBo.setCostDesc(costDetail.getCostDesc());
                costDetailBo.setCostDate(FormatUtil.formatDate2String(costDetail.getCostDate(), "yyyy-MM-dd"));
                // 设置分类信息
                CostCategoryBo costCategoryBo = new CostCategoryBo();
                CostCategory costCategory = costDetail.getCostCategory();
                costCategoryBo.setCateId(costCategory.getCateId());
                costCategoryBo.setCateName(costCategory.getCateName());
                costCategoryBo.setCateIcon(costCategory.getCateIcon());
                costDetailBo.setCostCategory(costCategoryBo);
                // 设置小组信息
                CostGroupBo costGroupBo = new CostGroupBo();
                CostGroup costGroup = costDetail.getCostGroup();
                costGroupBo.setGroupNo(costGroup.getGroupId());
                costGroupBo.setGroupName(costGroup.getGroupName());
                costGroupBo.setGroupCode(costGroup.getGroupCode());
                costDetailBo.setCostGroup(costGroupBo);
                costDetailBo.setCostId(costDetail.getCostId());
                costDetailBos.add(costDetailBo);
            }
        }
        return new BaseQueryResultBo<>(costDetailBos, costDetailPage.getNumber()+1, costDetailPage.getSize(), costDetailPage.getTotalElements(), costDetailPage.getTotalPages());
    }

    @Override
    public void deleteCostDetail(Integer costId) {
        User currentUser = webContext.getCurrentUser();
        costDetailRepository.deleteCostDetailById(costId, currentUser.getUserId());
    }

    /**
     * 用于分页使用的内部类
     */
    class CostDetailSpecification<CostDetail> implements Specification<CostDetail> {
        private BaseQueryBo baseQueryBo;
        public CostDetailSpecification(BaseQueryBo baseQueryBo){
            this.baseQueryBo = baseQueryBo;
        }

        @Override
        public Predicate toPredicate(Root<CostDetail> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();
            if (baseQueryBo instanceof WxQueryBo){
                WxQueryBo wxQueryBo = (WxQueryBo) baseQueryBo;
                if (wxQueryBo.getCriteria() != null){
                    for (WxQueryCriteriaBo criteriaBo : wxQueryBo.getCriteria()){
                        Path path = null;
                        Object value = criteriaBo.getValue();
                        switch (criteriaBo.getFieldName()){
                            case "groupId":
                                path = root.get("costGroup").get("groupId");
                                predicates.add(criteriaBuilder.equal(path, value));
                                break;
                            case "cleanId":
                                path = root.get("costClean").get("cleanId");
                                // 若没有值则查询为空的
                                if (value == null){
                                    predicates.add(criteriaBuilder.isNull(path));
                                } else {
                                    predicates.add(criteriaBuilder.equal(path, value));
                                }
                                break;
                            case "userId":
                                path = root.get("user").get("userId");
                                predicates.add(criteriaBuilder.equal(path, value));
                                break;
                            default:
                                break;

                        }
                    }
                }
            }
            Path deletePath = root.get("deleteId");
            predicates.add(criteriaBuilder.isNull(deletePath));
            Predicate[] pre = new Predicate[predicates.size()];
            criteriaQuery.where(predicates.toArray(pre));
            // 设置为消费时间倒序
            Path orderPath = root.get("costDate");
            Order order = new OrderImpl(orderPath, false);
            criteriaQuery.orderBy(order);
            return criteriaQuery.getRestriction();
        }
    }
}
