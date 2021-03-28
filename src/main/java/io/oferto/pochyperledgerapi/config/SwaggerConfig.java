package io.oferto.pochyperledgerapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2)				
				.apiInfo(apiInfo())
				.select().apis(RequestHandlerSelectors.basePackage("io.oferto.pochyperledgerapi.controller")).paths(PathSelectors.any())                          
		        .build();
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Hyperledger API")
				.description("Hyperledger API reference for developers")
				.termsOfServiceUrl("http://oferto.io/terms")
				.contact(new Contact("Oferto", "http://oferto.io", "info@oferto.io"))
				.build();
	}	
}
