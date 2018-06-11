package com.aalife.bo;

import java.util.List;

/**
 * @author mosesc
 * @date 2018-06-11
 */
public class WxQueryBo extends BaseQueryBo {
    private List<WxQueryCriteriaBo> criteria;

    public List<WxQueryCriteriaBo> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<WxQueryCriteriaBo> criteria) {
        this.criteria = criteria;
    }
}
