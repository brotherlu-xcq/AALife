package com.aalife.service;

import com.aalife.bo.CostCategoryBo;

import java.util.List;

/**
 * @author mosesc
 * @date 2018-06-11
 */
public interface CostCategoryService {
    /**
     * 获取所有的分类
     * @return
     */
    List<CostCategoryBo> findAllCategory();
}
