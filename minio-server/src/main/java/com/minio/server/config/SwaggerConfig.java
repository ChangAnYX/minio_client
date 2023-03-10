package com.minio.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger配置类
 *
 * @author bin
 * @since 1.0.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket createAPI() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				//指定哪个包下面生成接口文档
				.apis(RequestHandlerSelectors.basePackage("com.minio.server.controller"))
				.paths(PathSelectors.any())
				.build()
				.securitySchemes(securitySchemes())
				.securityContexts(securityContext());
	}

	private List<SecurityContext> securityContext() {
		List<SecurityContext> result = new ArrayList<>();
		result.add(getSecurityContext("/hello/.*"));
		return result;
	}

	private SecurityContext getSecurityContext(String regexPath) {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.regex(regexPath))
				.build();
	}

	private List<SecurityReference> defaultAuth() {
		List<SecurityReference> result = new ArrayList<>();
		AuthorizationScope authorizationScope = new AuthorizationScope("global","access Everything");
		AuthorizationScope[] scopes = new AuthorizationScope[1];
		scopes[0] = authorizationScope;
		result.add(new SecurityReference("Authorization", scopes));
		return result;
	}

	private List<ApiKey> securitySchemes() {
		List<ApiKey> result = new ArrayList<>();
		result.add(new ApiKey("Authorization", "Authorization", "header"));
		return result;
	}

	/**
	 * 文档基本信息
	 *
	 * @return
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.version("1.0")
				.title("网盘接口文档")
				.description("网盘接口文档")
				.contact(new Contact("bin", "http://localhost:8081/doc.html", "xxx@xxx.com"))
				.build();

	}

}
