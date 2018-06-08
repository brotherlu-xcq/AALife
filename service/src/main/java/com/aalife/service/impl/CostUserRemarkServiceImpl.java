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

/**
 * @author mosesc
 * @dare 2018-06-07
 */
@Service
@Transactional
public class CostUserRemarkServiceImpl implements CostUserRemarkService {
    @Autowired
    private CostUserRemarkRepository costUserRemarkRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebContext webContext;

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
    }
}
