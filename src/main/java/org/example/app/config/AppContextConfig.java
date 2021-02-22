package org.example.app.config;

import org.example.app.services.IdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.example.app") //<context:component-scan base-package="org.example.app"/>
public class AppContextConfig {

    //        <bean id="idProvider" class="org.example.app.services.IdProvider" scope="prototype"/>
    @Bean
    public IdProvider idProvider() {
        IdProvider idProvider = new IdProvider();
        return idProvider;
    }
}
