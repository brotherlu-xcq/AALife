package com.aalife.service.impl;

import com.aalife.bo.CostCategoryBo;
import com.aalife.dao.entity.CostCategory;
import com.aalife.dao.repository.CostCategoryRepository;
import com.aalife.service.CostCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mosesc
 * @date 2018-06-11
 */
@Service
@Transactional
public class CostCategoryServiceImpl implements CostCategoryService {
    @Autowired
    private CostCategoryRepository costCategoryRepository;

    @Override
    public List<CostCategoryBo> findAllCategory() {
        List<CostCategory> costCategories = costCategoryRepository.findAll();
        List<CostCategoryBo> costCategoryBos = new ArrayList<>();
        if (costCategories == null){
            return costCategoryBos;
        }
        for (CostCategory costCategory : costCategories){
            CostCategoryBo costCategoryBo = new CostCategoryBo();
            costCategoryBo.setCateId(costCategory.getCateId());
            costCategoryBo.setCateName(costCategory.getCateName());
            costCategoryBo.setCateIcon(costCategory.getCateIcon());
            costCategoryBos.add(costCategoryBo);
        }
        return costCategoryBos;
    }
}
