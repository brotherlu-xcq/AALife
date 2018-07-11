package com.aalife.service.impl;

import com.aalife.bo.CostUserRemarkBo;
import com.aalife.dao.entity.CostUserRemark;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.CostUserRemarkRepository;
import com.aalife.dao.repository.UserRepository;
import com.aalife.exception.BizException;
import com.aalife.service.CostUserRemarkService;
import com.aalife.service.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mosesc
 * @dare 2018-06-07
 */
@Service
@Transactional(rollbackFor = BizException.class)
public class CostUserRemarkServiceImpl implements CostUserRemarkService {
    @Autowired
    private CostUserRemarkRepository costUserRemarkRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebContext webContext;
    private static Map<String, String> remarkNames = new HashMap<>(8);

    @Override
    public void createRemarkName(CostUserRemarkBo costUserRemarkBo) {
        User sourceUser = webContext.getCurrentUser();
        Integer targetNo = costUserRemarkBo.getTargetNo();
        Integer sourceNo = sourceUser.getUserId();
        User targetUser = userRepository.findOne(targetNo);
        if (targetUser == null){
            throw new BizException("未查找到用户");
        }
        String remarkName = costUserRemarkBo.getRemarkName();
        if (StringUtils.isEmpty(remarkName)){
            throw new BizException("备注名不能为空");
        }
        CostUserRemark costUserRemark = costUserRemarkRepository.findRemarkBySourceAndTarget(sourceNo, targetNo);
        if (costUserRemark == null){
            costUserRemark = new CostUserRemark();
            costUserRemark.setSourceUser(sourceUser);
            costUserRemark.setTargetUser(targetUser);
            costUserRemark.setEntryId(sourceNo);
            costUserRemark.setEntryDate(new Date());
        }
        costUserRemark.setRemarkName(remarkName);
        costUserRemarkRepository.save(costUserRemark);
        remarkNames = new HashMap<>(8);
    }

    @Override
    public String getRemarkName(Integer sourceUserId, Integer targetUserId, String targetUserNickName) {
        String key = sourceUserId+"-"+targetUserId;
        String remarkName = remarkNames.get(key);
        if (remarkName == null){
            CostUserRemark costUserRemark = costUserRemarkRepository.findRemarkBySourceAndTarget(sourceUserId, targetUserId);
            remarkName = costUserRemark == null ? targetUserNickName : costUserRemark.getRemarkName();
            remarkNames.put(key, remarkName);
        }
        return remarkName;
    }
}
