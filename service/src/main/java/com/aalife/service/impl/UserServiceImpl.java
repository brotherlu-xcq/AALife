package com.aalife.service.impl;

import com.aalife.bo.UserOverviewBo;
import com.aalife.bo.WxUserBo;
import com.aalife.constant.SystemConstant;
import com.aalife.dao.entity.CostGroupUser;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.AppConfigRepository;
import com.aalife.dao.repository.CostDetailRepository;
import com.aalife.dao.repository.CostGroupUserRepository;
import com.aalife.dao.repository.UserRepository;
import com.aalife.exception.BizException;
import com.aalife.service.UserService;
import com.aalife.service.WXService;
import com.aalife.service.WebContext;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @author brother lu
 * @date 2018-06-04
 */

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebContext webContext;
    @Autowired
    private CostGroupUserRepository costGroupUserRepository;
    @Autowired
    private CostDetailRepository costDetailRepository;
    @Autowired
    private AppConfigRepository appConfigRepository;
    @Autowired
    private WXService wxService;

    @Override
    public void login(WxUserBo wxUser) {
        if (StringUtils.isEmpty(wxUser.getWxCode())){
            throw new AuthenticationException("登陆失败");
        }
        User wxUserInfo;
        try {
            wxUserInfo = wxService.getWXUserInfo(wxUser);
        } catch (Exception e){
            throw new BizException("登录失败，错误原因："+e.getMessage());
        }
        String wxOpenId = wxUserInfo.getWxOpenId();
        // 查询没有该用户则添加一条记录
        User user = userRepository.findUserWithOpenId(wxOpenId);
        if (user == null){
            user = new User();
            user.setEntryId(SystemConstant.SYSTEM_ID);
            user.setEntryDate(new Date());
            user.setWxOpenId(wxOpenId);
        }
        user.setAvatarUrl(wxUserInfo.getAvatarUrl());
        user.setNickName(wxUserInfo.getNickName());
        userRepository.save(user);
        UsernamePasswordToken token = new UsernamePasswordToken(wxOpenId, (String)null);
        SecurityUtils.getSubject().login(token);
    }

    @Override
    public void loginAsUser(String openId) {
        String env = appConfigRepository.findAppConfigValueByName("AALIFE", "ENV");
        if (StringUtils.isEmpty(env)){
            throw new BizException("此接口暂时无法访问");
        }
        UsernamePasswordToken token = new UsernamePasswordToken(openId, (String)null);
        SecurityUtils.getSubject().login(token);
    }

    @Override
    public UserOverviewBo getUserOverview() {
        UserOverviewBo userOverviewBo = new UserOverviewBo();
        User user = webContext.getCurrentUser();
        Integer userId = user.getUserId();
        // 设置用户基本信息
        userOverviewBo.setAvatarUrl(user.getAvatarUrl());
        userOverviewBo.setNickName(user.getNickName());
        // 获取用户总消费
        List<CostGroupUser> costGroupUsers = costGroupUserRepository.findCostGroupUserByUser(userId);
        if (costGroupUsers == null || costGroupUsers.size() == 0){
            userOverviewBo.setLeftCost(new BigDecimal(0));
            userOverviewBo.setTotalCost(new BigDecimal(0));
            return userOverviewBo;
        }
        BigDecimal totalCost = new BigDecimal(0);
        BigDecimal leftCost = new BigDecimal(0);
        for (CostGroupUser costGroupUser : costGroupUsers){
            Integer groupId = costGroupUser.getCostGroup().getGroupId();
            List<CostGroupUser> costGroupUsersCount = costGroupUserRepository.findCostGroupByGroup(groupId);
            if (costGroupUsersCount.size() == 1){
                continue;
            }
            BigDecimal groupCost = costDetailRepository.findTotalCostByUserAndGroup(groupId, userId);
            BigDecimal groupTotalCost = costDetailRepository.findTotalCostByGroup(groupId);
            // 计算总消费
            groupCost = groupCost == null ? new BigDecimal(0) : groupCost;
            totalCost = totalCost.add(groupCost);
            // 计算平均消费
            groupTotalCost = groupTotalCost == null ? new BigDecimal(0) : groupTotalCost;
            BigDecimal count = new BigDecimal(costGroupUsersCount.size());
            BigDecimal groupAverageCost = groupTotalCost.divide(count, 2);
            BigDecimal groupLeftCost = groupAverageCost.subtract(groupCost);
            leftCost = leftCost.add(groupLeftCost);
            logger.info("{userId:"+userId+", groupId:"+groupId+", userCount:"+count+", groupTotalCost:"+groupTotalCost+", totalCost:"+groupCost+", averageCost:"+groupAverageCost+", leftCost:"+groupLeftCost+"}");
        }
        userOverviewBo.setLeftCost(leftCost);
        userOverviewBo.setTotalCost(totalCost);
        return userOverviewBo;
    }
}
