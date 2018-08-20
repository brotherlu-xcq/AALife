package com.aalife.web.publicapi;

import com.aalife.bo.ArticleOverviewBo;
import com.aalife.bo.BaseQueryResultBo;
import com.aalife.bo.WxQueryBo;
import com.aalife.service.ArticleService;
import com.aalife.web.util.JsonEntity;
import com.aalife.web.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mosesc
 * @date 2018-08-20
 */
@RestController
@RequestMapping(value = "/public/api")
@Api
public class ArticleApi {
    @Autowired
    private ArticleService articleService;

    @PostMapping(value = "/articles/overview")
    public JsonEntity<BaseQueryResultBo<ArticleOverviewBo>> getArticleOverview(@RequestBody WxQueryBo wxQueryBo){
        return ResponseHelper.createInstance(articleService.getArticleOverview(wxQueryBo));
    }
}
