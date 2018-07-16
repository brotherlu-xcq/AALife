package com.aalife.web.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 *
 * @author brotherlu
 * @date 2018-05-30
 */
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerConfig {
    @Value("${swagger.title}")
    private String title;
    @Value("${swagger.description}")
    private String description;
    @Value("${swagger.url}")
    private String url;
    @Value("${swagger.version}")
    private String version;
    @Value("${swagger.enable}")
    private Boolean enable;
    @Value("${swagger.author.email}")
    private String email;
    @Value("${swagger.author.name}")
    private String name;

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enable)
                .apiInfo(getApiInfo())
                .select()
                // 对有API注解的api进行解析
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 对哪些路径进行监控
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo getApiInfo(){
        Contact author = new Contact(name, email, null);
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(url)
                .contact(author)
                .version(version)
                .build();
    }
}
