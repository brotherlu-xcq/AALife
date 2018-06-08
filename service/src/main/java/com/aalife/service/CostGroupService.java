package com.aalife.service;

import com.aalife.bo.CostGroupBo;
import com.aalife.bo.CostGroupOverviewBo;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.entity.User;

import java.util.List;

/**
 * @author brother lu
 * @date 2018-06-05
 */
public interface CostGroupService {
    /**
     * 创建一个新的分组
     * @param groupName
     * @return
     */
    Integer createNewCostGroup(String groupName);

    /**
     * 更新账单
     * @param costGroupBo
     */
    void updateCostGroup(CostGroupBo costGroupBo);

    /**
     * 根据code去寻找对应得group
     * @param code
     * @return
     */
    CostGroupBo findCostGroupByCode(String code);

    /**
     * 根据group id删除账单
     * @param groupId
     */
    void deleteCostGroup(Integer groupId);

    /**
     * 退出账单
     * @param groupId
     */
    void leaveCostGroup(Integer groupId);

    /**
     * 分配管理员
     * @param groupId
     * @param userId
     */
    void assignGroupAdmin(Integer groupId, Integer userId);

    /**
     * 从账单中移除某一成员
     * @param groupId
     * @param userId
     */
    void deleteCostGroupUser(Integer groupId, Integer userId);


    /**
     * 根据id获取每一组的消费概况
     * @param groupId
     * @return
     */
    CostGroupOverviewBo listCostGroupOverview(Integer groupId);

    /**
     * 查询当前用户所在的组
     * @return
     */
    List<CostGroupBo> listMyGroups();
}
