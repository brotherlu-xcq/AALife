package com.aalife.service.impl;

import com.aalife.bo.*;
import com.aalife.constant.SystemConstant;
import com.aalife.dao.entity.AppConfig;
import com.aalife.dao.entity.CostCategory;
import com.aalife.dao.entity.CostClean;
import com.aalife.dao.entity.CostCleanUser;
import com.aalife.dao.entity.CostDetail;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.entity.CostGroupUser;
import com.aalife.dao.entity.CostUserRemark;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.AppConfigRepository;
import com.aalife.dao.repository.CostCategoryRepository;
import com.aalife.dao.repository.CostCleanRepository;
import com.aalife.dao.repository.CostCleanUserRepository;
import com.aalife.dao.repository.CostDetailRepository;
import com.aalife.dao.repository.CostGroupRepository;
import com.aalife.dao.repository.CostGroupUserRepository;
import com.aalife.dao.repository.CostUserRemarkRepository;
import com.aalife.exception.BizException;
import com.aalife.service.*;
import com.aalife.utils.DateUtil;
import com.aalife.utils.FormatUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.UnauthorizedException;
import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
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
@Transactional(rollbackFor = Exception.class)
public class CostDetailServiceImpl implements CostDetailService {
    private static Logger logger = Logger.getLogger(CostDetailServiceImpl.class);
    @Autowired
    private CostDetailRepository costDetailRepository;
    @Autowired
    private CostCategoryRepository costCategoryRepository;
    @Autowired
    private CostGroupRepository costGroupRepository;
    @Autowired
    private CostCleanRepository costCleanRepository;
    @Autowired
    private CostCleanUserRepository costCleanUserRepository;
    @Autowired
    private CostUserRemarkService costUserRemarkService;
    @Autowired
    private AppConfigRepository appConfigRepository;
    @Autowired
    private WebContext webContext;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private CostGroupUserRepository costGroupUserRepository;
    @Autowired
    private NotificationService notificationService;

    @Override
    public void createNewCostDetail(NewCostDetailBo costDetailBo) {
        User currentUser = webContext.getCurrentUser();
        String costDesc = costDetailBo.getCostDesc() == null ? null : costDetailBo.getCostDesc().trim();
        CostCategory costCategory = costCategoryRepository.findOne(costDetailBo.getCateId());
        String costDateStr = costDetailBo.getCostDate();
        Date costDate;
        try {
            costDate = FormatUtil.parseString2Date(costDateStr, SystemConstant.DATE_PATTERN);
        } catch (ParseException e) {
            throw new BizException("时间格式错误，请参照 yyyy-MM-dd", e);
        }
        if (costCategory == null){
            throw new BizException("未查询到对应得分类");
        }
        CostGroup costGroup = costGroupRepository.findGroupById(costDetailBo.getGroupId());
        if (costGroup == null){
            throw new BizException("未查询到群组");
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
    public Integer cleanCostDetail(Integer groupId, String comment) {
        comment = comment == null ? comment : comment.trim();
        if (StringUtils.isEmpty(comment)){
            throw new BizException("结算备注不能为空");
        }
        //校验是否有数据
        int unCleanCount = costDetailRepository.findUnCleanDetailCount(groupId);
        if (unCleanCount == 0){
            throw new BizException("没有可以结算的记录");
        }
        // 创建结算记录
        CostGroup costGroup = costGroupRepository.findGroupById(groupId);
        User currentUser = webContext.getCurrentUser();
        CostClean costClean = new CostClean();
        costClean.setUser(currentUser);
        costClean.setCostGroup(costGroup);
        costClean.setComment(comment);
        costClean.setEntryId(currentUser.getUserId());
        costClean.setEntryDate(new Date());
        costCleanRepository.save(costClean);
        Integer cleanId = costClean.getCleanId();
        // 创建结算时的用户列表
        List<CostGroupUser> costGroupUsers = costGroupUserRepository.findCostGroupByGroup(groupId);
        List<CostCleanUser> costCleanUsers = new ArrayList<>();
        costGroupUsers.forEach(costGroupUser -> {
            CostCleanUser costCleanUser = new CostCleanUser();
            costCleanUser.setCostClean(costClean);
            costCleanUser.setUser(costGroupUser.getUser());
            costCleanUsers.add(costCleanUser);
        });
        costCleanUserRepository.save(costCleanUsers);
        // 开始结算
        costDetailRepository.cleanCostDetailByGroup(groupId, cleanId);
        // 异步发送微信信息
        try {
            BigDecimal groupTotalCost = costDetailRepository.findTotalCostByGroup(groupId, cleanId);
            groupTotalCost = groupTotalCost == null ? new BigDecimal(0) : groupTotalCost;
            BigDecimal count = new BigDecimal(costGroupUsers.size());
            BigDecimal averageCost = groupTotalCost.divide(count, 2);
            // 初始化发送信息的内容
            BigDecimal zero = new BigDecimal(0);
            costGroupUsers.forEach(costGroupUser -> {
                BigDecimal userCost = costDetailRepository.findTotalCostByUserAndGroup(groupId, costGroupUser.getUser().getUserId(), cleanId);
                userCost = userCost == null ? new BigDecimal(0) : userCost;
                BigDecimal leftCost = userCost.subtract(averageCost);
                WxNotificationDetailBo data = new WxNotificationDetailBo();
                Map<String, Object> groupName = new HashMap<>(2);
                groupName.put("value", costGroup.getGroupName());
                data.setKeyword1(groupName);
                Map<String, Object> userName = new HashMap<>(2);
                userName.put("value", currentUser.getNickName());
                data.setKeyword2(userName);
                Map<String, Object> averageCostTemp = new HashMap<>(2);
                averageCostTemp.put("value", userCost.doubleValue() + "元");
                data.setKeyword3(averageCostTemp);
                Map<String, Object> groupTotalCostTemp = new HashMap<>(2);
                groupTotalCostTemp.put("value", averageCost.doubleValue() + "元");
                data.setKeyword4(groupTotalCostTemp);
                Map<String, Object> leftCostTemp = new HashMap<>(2);
                String sax = "收";
                if (leftCost.compareTo(zero) < 0){
                    leftCost = zero.subtract(leftCost);
                    sax = "付";
                }
                leftCostTemp.put("value", sax+leftCost.doubleValue() + "元");
                data.setKeyword5(leftCostTemp);
                String tail = "?groupId="+groupId+"&cleanId="+cleanId;
                notificationService.sendWxNotification(costGroupUser.getUser().getUserId(), SystemConstant.CLEAN_RESULT, data, tail);
            });
        } catch (Exception e){
            logger.error("异步发送信息失败", e);
        }
        return cleanId;
    }

    @Override
    public BaseQueryResultBo<CostDetailBo> listCostDetail(WxQueryBo wxQueryBo) {
        PageRequest pageRequest = new PageRequest(wxQueryBo.getPage() -1, wxQueryBo.getSize());
        Specification<CostDetail> specification = new CostDetailSpecification(wxQueryBo);
        Page<CostDetail> costDetailPage = costDetailRepository.findAll(specification, pageRequest);
        List<CostDetail> costDetails = costDetailPage.getContent();
        List<CostDetailBo> costDetailBos = new ArrayList<>();
        User currentUser = webContext.getCurrentUser();
        Integer currentUserId = currentUser.getUserId();
        if (costDetails != null){
            for (CostDetail costDetail : costDetails){
                CostDetailBo costDetailBo = new CostDetailBo();
                // 设置用户信息
                User user = costDetail.getUser();
                Integer targetUserId = user.getUserId();
                String nickName = user.getNickName();
                ExtendUserBo userBo = new ExtendUserBo();
                userBo.setAvatarUrl(user.getAvatarUrl());
                userBo.setNickName(nickName);
                userBo.setUserId(user.getUserId());
                String remarkName = costUserRemarkService.getRemarkName(currentUserId, targetUserId, nickName);
                userBo.setRemarkName(remarkName);
                // 设置基本消费信息
                costDetailBo.setUser(userBo);
                costDetailBo.setCostMoney(costDetail.getCostMoney());
                costDetailBo.setCostDesc(costDetail.getCostDesc());
                costDetailBo.setCostDate(FormatUtil.formatDate2CommonString(costDetail.getCostDate(), SystemConstant.DATE_PATTERN));
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
                // 设置结算信息
                CostClean costClean = costDetail.getCostClean();
                if (costClean != null){
                    CostCleanBo costCleanBo = new CostCleanBo();
                    costCleanBo.setCleanDate(FormatUtil.formatDate2CommonString(costClean.getEntryDate(), SystemConstant.DATE_PATTERN));
                    costCleanBo.setComment(costClean.getComment());
                    User cleanUser = costClean.getUser();
                    ExtendUserBo cleanUserBo = new ExtendUserBo();
                    Integer cleanUserId = cleanUser.getUserId();
                    String cleanNickName = cleanUser.getNickName();
                    cleanUserBo.setUserId(cleanUserId);
                    cleanUserBo.setNickName(cleanNickName);
                    cleanUserBo.setAvatarUrl(cleanUser.getAvatarUrl());
                    // 设置昵称
                    String cleanRemarkName = costUserRemarkService.getRemarkName(cleanUserId, cleanUserId, cleanNickName);
                    cleanUserBo.setRemarkName(cleanRemarkName);
                    costCleanBo.setUser(cleanUserBo);
                    costDetailBo.setCostClean(costCleanBo);
                }
            }
        }
        return new BaseQueryResultBo<>(costDetailBos, costDetailPage.getNumber()+1, costDetailPage.getSize(), costDetailPage.getTotalElements(), costDetailPage.getTotalPages());
    }

    @Override
    public void deleteCostDetail(Integer groupId, Integer costId) {
        User currentUser = webContext.getCurrentUser();
        CostDetail costDetail = costDetailRepository.findOne(costId);
        if (costDetail == null || costDetail.getDeleteId() != null){
            throw new BizException("未查询到消费记录");
        }
        if (costDetail.getCostClean() != null){
            throw new BizException("此消费记录已经结算");
        }
        Integer currentUserId = currentUser.getUserId();
        // 只有自己或则是管理员可以删除当前用户的记录
        CostGroupUser costGroupUser = costGroupUserRepository.findCostGroupByUserAndGroup(currentUserId, groupId);
        if (!currentUserId.equals(costDetail.getUser().getUserId()) && costGroupUser.getAdmin().equals('N')){
            throw new BizException("只能删除自己的消费记录");
        }
        costDetailRepository.deleteCostDetailById(costId, currentUserId);
    }

    @Override
    public CostDetailBo getCostDetailByInvoice(Integer groupId, MultipartFile invoice) {
        if (invoice == null || invoice.getSize() == 0){
            return null;
        }
        // 初始化token，如果token存在且小于28天，则用Appconfig数据，否则从新生成
        AppConfig tokenConfig = appConfigRepository.findAppConfigByName("INVOICE", "TOKEN");
        tokenConfig = tokenConfig == null ? new AppConfig() : tokenConfig;
        Date entryDate = tokenConfig.getEntryDate();
        Date today = new Date();
        String token = tokenConfig.getConfigValue();
        // 若存在token，且token还有至少28天，那么不会继续请求token。百度语音token保存时间：30天
        if (entryDate == null || DateUtil.getHoursGap(entryDate, today)/24 >= 30){
            token = invoiceService.getToken();
            tokenConfig.setEntryId(SystemConstant.SYSTEM_ID);
            tokenConfig.setAppName("INVOICE");
            tokenConfig.setConfigName("TOKEN");
            tokenConfig.setConfigValue(token);
            tokenConfig.setEntryDate(today);
            appConfigRepository.save(tokenConfig);
        }
        String fileName = invoice.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        byte[] content;
        try {
            content = invoice.getBytes();
        } catch (IOException e){
            throw new BizException(e);
        }
        String result = invoiceService.getInvoiceContent(token, fileType, content);
        CostDetailBo costDetail = new CostDetailBo();
        costDetail.setCostDesc(result);
        return costDetail;
    }

    /**
     * 用于分页查询使用的内部类，主要为查询的条件
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
