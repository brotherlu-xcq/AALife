package com.aalife.service.impl;

import com.aalife.bo.BaseQueryBo;
import com.aalife.bo.BaseQueryResultBo;
import com.aalife.bo.CostCategoryBo;
import com.aalife.bo.CostCleanBo;
import com.aalife.bo.CostDetailBo;
import com.aalife.bo.CostGroupBo;
import com.aalife.bo.ExtendUserBo;
import com.aalife.bo.NewCostDetailBo;
import com.aalife.bo.WxQueryBo;
import com.aalife.bo.WxQueryCriteriaBo;
import com.aalife.constant.SystemConstant;
import com.aalife.dao.entity.AppConfig;
import com.aalife.dao.entity.CostCategory;
import com.aalife.dao.entity.CostClean;
import com.aalife.dao.entity.CostDetail;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.entity.CostUserRemark;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.AppConfigRepository;
import com.aalife.dao.repository.CostCategoryRepository;
import com.aalife.dao.repository.CostCleanRepository;
import com.aalife.dao.repository.CostDetailRepository;
import com.aalife.dao.repository.CostGroupRepository;
import com.aalife.dao.repository.CostUserRemarkRepository;
import com.aalife.exception.BizException;
import com.aalife.service.CostDetailService;
import com.aalife.service.InvoiceService;
import com.aalife.service.WebContext;
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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
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
    private CostUserRemarkRepository costUserRemarkRepository;
    @Autowired
    private AppConfigRepository appConfigRepository;
    @Autowired
    private WebContext webContext;
    @Autowired
    private InvoiceService invoiceService;

    @Override
    public void createNewCostDetail(NewCostDetailBo costDetailBo) {
        User currentUser = webContext.getCurrentUser();
        String costDesc = costDetailBo.getCostDesc();
        CostCategory costCategory = costCategoryRepository.findOne(costDetailBo.getCateId());
        String costDateStr = costDetailBo.getCostDate();
        Date costDate;
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
        //校验是否有数据
        int unCleanCount = costDetailRepository.findUnCleanDetailCount(groupId);
        if (unCleanCount == 0){
            throw new BizException("没有可以结算的记录");
        }
        // 创建结算记录
        User currentUser = webContext.getCurrentUser();
        CostClean costClean = new CostClean();
        costClean.setUser(currentUser);
        costClean.setCostGroup(costGroupRepository.findGroupById(groupId));
        costClean.setComment(comment);
        costClean.setEntryId(currentUser.getUserId());
        costClean.setEntryDate(new Date());
        costCleanRepository.save(costClean);
        // 开始结算
        costDetailRepository.cleanCostDetailByGroup(groupId, costClean.getCleanId());
    }

    @Override
    public BaseQueryResultBo<CostDetailBo> listCostDetail(WxQueryBo wxQueryBo) {
        PageRequest pageRequest = new PageRequest(wxQueryBo.getPage() -1, wxQueryBo.getSize());
        Specification<CostDetail> specification = new CostDetailSpecification(wxQueryBo);
        Page<CostDetail> costDetailPage = costDetailRepository.findAll(specification, pageRequest);
        List<CostDetail> costDetails = costDetailPage.getContent();
        List<CostDetailBo> costDetailBos = new ArrayList<>();
        // 用户存放不至于每次重复查
        Map<String, String> remarkNames = new HashMap<>(8);
        User currentUser = webContext.getCurrentUser();
        Integer currentUserId = currentUser.getUserId();
        if (costDetails != null){
            for (CostDetail costDetail : costDetails){
                CostDetailBo costDetailBo = new CostDetailBo();
                // 设置用户信息
                User user = costDetail.getUser();
                ExtendUserBo userBo = new ExtendUserBo();
                userBo.setAvatarUrl(user.getAvatarUrl());
                userBo.setNickName(user.getNickName());
                userBo.setUserId(user.getUserId());
                String key = currentUserId + "-"+user.getUserId();
                String remarkName = remarkNames.get(key);
                if (remarkName == null){
                    CostUserRemark costUserRemark = costUserRemarkRepository.findRemarkBySourceAndTarget(currentUserId, user.getUserId());
                    remarkName = costUserRemark == null ? user.getNickName() : costUserRemark.getRemarkName();
                    remarkNames.put(key, remarkName);
                }
                userBo.setRemarkName(remarkName);
                // 设置基本消费信息
                costDetailBo.setUser(userBo);
                costDetailBo.setCostMoney(costDetail.getCostMoney());
                costDetailBo.setCostDesc(costDetail.getCostDesc());
                costDetailBo.setCostDate(FormatUtil.formatDate2String(costDetail.getCostDate(), SystemConstant.DATEPATTERN));
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
                    costCleanBo.setCleanDate(FormatUtil.formatDate2String(costClean.getEntryDate(), SystemConstant.DATEPATTERN));
                    costCleanBo.setComment(costClean.getComment());
                    User cleanUser = costClean.getUser();
                    ExtendUserBo cleanUserBo = new ExtendUserBo();
                    Integer cleanUserId = cleanUser.getUserId();
                    cleanUserBo.setUserId(cleanUserId);
                    cleanUserBo.setNickName(cleanUser.getNickName());
                    cleanUserBo.setAvatarUrl(cleanUser.getAvatarUrl());
                    String cleanKey = currentUserId + "-"+cleanUserId;
                    String cleanRemarkName = remarkNames.get(cleanKey);
                    // 设置昵称
                    if (cleanRemarkName == null){
                        CostUserRemark cleanCostUserRemark = costUserRemarkRepository.findRemarkBySourceAndTarget(cleanUserId, cleanUserId);
                        cleanRemarkName = cleanRemarkName == null ? cleanUser.getNickName() : cleanCostUserRemark.getRemarkName();
                        remarkNames.put(cleanKey, cleanRemarkName);
                    }
                    cleanUserBo.setRemarkName(cleanRemarkName);
                    costCleanBo.setUser(cleanUserBo);
                    costDetailBo.setCostClean(costCleanBo);
                }
            }
        }
        return new BaseQueryResultBo<>(costDetailBos, costDetailPage.getNumber()+1, costDetailPage.getSize(), costDetailPage.getTotalElements(), costDetailPage.getTotalPages());
    }

    @Override
    public void deleteCostDetail(Integer costId) {
        User currentUser = webContext.getCurrentUser();
        CostDetail costDetail = costDetailRepository.findOne(costId);
        if (costDetail == null || costDetail.getDeleteId() != null){
            throw new BizException("未查询到消费记录");
        }
        if (costDetail.getCostClean() != null){
            throw new BizException("此消费记录已经结算");
        }
        if (!currentUser.equals(costDetail.getUser().getUserId())){
            throw new UnauthorizedException();
        }
        costDetailRepository.deleteCostDetailById(costId, currentUser.getUserId());
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
        // 如果不是这三种格式的文件，那么需要转码
//        if (!fileType.equalsIgnoreCase("pcm") || !fileType.equalsIgnoreCase("wav") || !fileType.equalsIgnoreCase("amr")){
//            File tempInvoice = new File(UUIDUtil.get16BitUUID())
//            InvoiceConvertUtil.getPcmAudioInputStream(invoice.);
//        }
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
