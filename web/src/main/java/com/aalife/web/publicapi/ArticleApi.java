package com.aalife.web.publicapi;

import com.aalife.bo.ArticleBo;
import com.aalife.bo.ArticleDetailBo;
import com.aalife.bo.ArticleOverviewBo;
import com.aalife.bo.BaseQueryResultBo;
import com.aalife.bo.WxQueryBo;
import com.aalife.service.ArticleService;
import com.aalife.web.util.JsonEntity;
import com.aalife.web.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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

    @GetMapping(value = "/article/{articleId}")
    public JsonEntity<ArticleDetailBo> getArticleDetail(@PathVariable(value = "articleId") Integer articleId){
        return ResponseHelper.createInstance(articleService.getArticleById(articleId));
    }

    @PostMapping(value = "/article")
    @RequiresAuthentication
    public JsonEntity<Map<String, Object>> createNewArticle(@RequestBody ArticleBo articleBo){
        return ResponseHelper.createInstance(articleService.createNewArticle(articleBo));
    }
}
