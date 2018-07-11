package com.aalife.service;

import com.aalife.bo.CostUserRemarkBo;

/**
 * @author mosesc
 * @date 2018-06-07
 */
public interface CostUserRemarkService {
    /**
     * 创建新的备注
     * @param costUserRemarkBo
     */
    void createRemarkName(CostUserRemarkBo costUserRemarkBo);

    /**
     * 获取用户的昵称
     * @param sourceUserId
     * @param targetUserId
     * @param targetUserNickName
     * @return
     */
    String getRemarkName(Integer sourceUserId, Integer targetUserId, String targetUserNickName);
}
