package com.aalife.service;

import java.util.Map;

/**
 * @author mosesc
 * @date 2018-07-16
 */
public interface TemplateService {
    /**
     * 解析邮件模板
     * @param templateFile
     * @param model
     * @param isFile
     * @return
     */
    String process(String templateFile, Map<String, Object> model, boolean isFile);
}
