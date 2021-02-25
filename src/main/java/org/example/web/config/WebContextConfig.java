package org.example.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
@ComponentScan(basePackages = "org.example.*") //<context:component-scan base-package="org.example.web"/>
@EnableWebMvc //<mvc:annotation-driven/
public class WebContextConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //<mvc:resources mapping="/**" location="classpath:images"/>
        registry.addResourceHandler("/**").addResourceLocations("classpath:/images");
    }

    /*        <bean id="templateResolver"
        class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">
            <property name="prefix" value="/WEB-INF/views/"/>
            <property name="suffix" value=".html"/>
            <!-- HTML - значение по умолчанию. Добавлено здесь для большей ясности.              -->
            <property name="templateMode" value="HTML"/>
            <!-- Cache страниц по-умолчанию имеет значение true. Установите в false, если хотите -->
            <!-- чтобы шаблоны автоматически обновлялись при их изменении.                       -->
            <property name="cacheable" value="true"/>
            <property name="characterEncoding" value="UTF-8"/>
        </bean>*/

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");

        return templateResolver;
    }

//        <bean id="templateEngine"
//    class="org.thymeleaf.spring5.SpringTemplateEngine">
//        <property name="templateResolver" ref="templateResolver"/>
//        <!-- Включение компилятора SpringEL в Spring 4.2.4 или новее может ускорить -->
//        <!-- выполнение в большинстве сценариев, но может быть несовместимо с конкретными -->
//        <!-- случаями, когда выражения на одной странице повторно используются в разных данных, -->
//        <!-- так что этот флаг по умолчанию имеет значение «false» для более безопасной обратной -->
//        <!-- совместимости. -->
//        <property name="enableSpringELCompiler" value="true"/>
//    </bean>

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    //        <bean class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
//        <property name="templateEngine" ref="templateEngine"/>
//        <property name="order" value="1"/>
//    </bean>

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        return viewResolver;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(5_000_000);
        return multipartResolver;
    }
}
