package com.wbp.boot.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

/**
 *  配置freemarker模板引入shiro标签
 *
 *      1.引入依赖
 *          <dependency>
 *             <groupId>net.mingsoft</groupId>
 *             <artifactId>shiro-freemarker-tags</artifactId>
 *             <version>1.0.0</version>
 *         </dependency>
 *
 *     2.创建该配置类
 *
 */
@Configuration
public class ShiroTagFreeMarkerConfigurer {

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException, TemplateException {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        //ftl文件路径
       // freeMarkerConfigurer.setTemplateLoaderPath("classpath:templates/");
        freeMarkerConfigurer.setTemplateLoaderPaths("classpath:templates/");

        freemarker.template.Configuration configuration = freeMarkerConfigurer.createConfiguration();
        configuration.setDefaultEncoding("UTF-8");
        //freemarker引入shiro标签库
        configuration.setSharedVariable("shiro", new ShiroTags());
        freeMarkerConfigurer.setConfiguration(configuration);
        return freeMarkerConfigurer;
    }
}


