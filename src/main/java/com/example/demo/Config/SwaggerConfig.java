package com.example.demo.Config;


//mport org.springdoc.core.SpringDocConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/*
@Configuration
@EnableSwagger2

public class SwaggerConfig {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2).
                select().apis(RequestHandlerSelectors.basePackage("com.example.demo.Controller")).
                paths(PathSelectors.any()).build();
    }
}


 */
