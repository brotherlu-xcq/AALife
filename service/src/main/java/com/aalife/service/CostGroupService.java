package com.aalife.service;

import com.aalife.dao.entity.User;

/**
 * @author brother lu
 * @date 2018-06-05
 */
public interface CostGroupService {
    /**
     * 创建一个新的分组
     * @param user
     * @return
     */
    Integer createNewCostGroup(User user);
}
