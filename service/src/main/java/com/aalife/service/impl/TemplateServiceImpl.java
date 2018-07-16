package com.aalife.service.impl;

import com.aalife.constant.SystemConstant;
import com.aalife.dao.repository.AppConfigRepository;
import com.aalife.exception.BizException;
import com.aalife.service.TemplateService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.Map;

/**
 * @author mosesc
 * @date 2018-07-16
 */
@Component
public class TemplateServiceImpl implements TemplateService {
    private static Logger logger = Logger.getLogger(TemplateService.class);
    @Autowired
    private FreeMarkerConfigurer configurer;
    @Autowired
    private AppConfigRepository appConfigRepository;

    @Override
    public String process(String templateFile, Map<String, Object> model, boolean isFile){
        try {
            Configuration configuration = configurer.getConfiguration();
            String content;
            Template template;
            if (isFile){
                template = configuration.getTemplate(templateFile);
            } else {
                StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
                String templateContent = appConfigRepository.findAppConfigValueByName(SystemConstant.MAIL_TEMP, templateFile);
                stringTemplateLoader.putTemplate(templateFile, templateContent);
                configuration.setTemplateLoader(stringTemplateLoader);
                template = configuration.getTemplate(templateFile, SystemConstant.UTF8);
            }
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return content;
        } catch (IOException e) {
            logger.error("获取邮件模板失败", e);
            throw new BizException("获取邮件模板失败", e);
        } catch (TemplateException e) {
            logger.error("解析邮件模板失败", e);
            throw new BizException("解析邮件模板失败", e);
        }
    }
}
