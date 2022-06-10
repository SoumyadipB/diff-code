package com.ericsson.isf.config;

import java.util.Arrays;
import java.util.List;

import io.jsonwebtoken.lang.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import com.ericsson.isf.controller.AccessManagementController;

@EnableSwagger2
@PropertySource("classpath:properties/swagger.properties")
@ComponentScan(basePackageClasses = AccessManagementController.class)
@Configuration

public class SwaggerConfig {
	
	private static final String version = "1.0";
	private static final String license = "LICENSE";
	private static final String title = "JAVA API List";
	private static final String description = "JAVA API List";
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(title).description(description)
				.license(license).version(version).build();
	}
	
	@Bean
	public Docket api() {                
	    return new Docket(DocumentationType.SWAGGER_2)          
	      .select()
	      .build()
	      .apiInfo(apiInfo()).securitySchemes(Arrays.asList(apiKey()))
          .securityContexts(Arrays.asList(securityContext()))
          .apiInfo(apiInfo());
	}
	
	 private ApiKey apiKey() {
	        return new ApiKey("apiKey", "Authorization", "header");
	    }
	 
	
	 private SecurityContext securityContext() {
	        return SecurityContext.builder()
	                .securityReferences(defaultAuth())
	                .forPaths(PathSelectors.regex("/api.*"))
	                .build();
	    }
	 
	    private List<SecurityReference> defaultAuth() {
	        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
	        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	        authorizationScopes[0] = authorizationScope;
	        return Arrays.asList(new SecurityReference("apiKey", authorizationScopes));
	    }
}
	