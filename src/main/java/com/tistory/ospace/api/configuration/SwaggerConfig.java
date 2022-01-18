package com.tistory.ospace.api.configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.tistory.ospace.api.util.Errors;
import com.tistory.ospace.common.FileUtils;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// Reference:
// http://springfox.github.io/springfox/docs/snapshot/
// http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api

// doc url: http://localhost:8010/v2/api-docs
// ui url: http://localhost:8010/swagger-ui/
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .globalRequestParameters(newGlobalOperationParams())
          .select()
          .apis(RequestHandlerSelectors.basePackage(getBasePackage()))
          .paths(PathSelectors.any())
          .build()
          .apiInfo(newApiInfo())
          .useDefaultResponseMessages(false)
          .globalResponses(HttpMethod.POST, newResponseMessages());
    }
	
	private String getBasePackage() {
		String packagePath = this.getClass().getName();
	    int pos = packagePath.lastIndexOf('.', packagePath.lastIndexOf('.', packagePath.length())-1);
	    return packagePath.substring(0, pos)+".controller";
	}
	
	private List<RequestParameter> newGlobalOperationParams(){
        return Arrays.asList(
			new RequestParameterBuilder()
			.name("authKey")
			.description("(샘플) authKey 입력")
			.in(ParameterType.HEADER)
			.required(true)
			.query(q->q.model(m->m.scalarModel(ScalarType.STRING)))
			//.contentModel(new ModelSpecification("string"))
			//.parameterType("header").build();
			.build()
		);
    }
	
	private List<Response> newResponseMessages(){
		return Arrays.asList(
			new ResponseBuilder()
        		.code("500")
        		.description("internal error - <a href=\"#status\">Reference Status</a>")
        		.build()
		);
    }
	
	private ApiInfo newApiInfo() {
		return new ApiInfoBuilder()
                .title("Simple API")
                .description(newDescription())
                .build();
	}
	
	private String newDescription() {
		String header = 
				"<div class=\"opblock\"><div class=\"responses-inner\">" +
				"<details><summary>Status<sup><sub>(click to open)</sub></sup></summary>" +
				"<div class=\"response-col_description__inner\" style=\"width:100%\">\r\n\r\n" +
				"| Code | Description |\r\n" +
				"|:-|:-|\r\n";
		
		String tail =
				"</div>" +
				"</details>" +
				"</div></div>";
		
		String description = null;
		try {
			description = FileUtils.readResource("messages/description.md");
		} catch (IOException e) {
			description = "failed to load a description\r\n";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(description)
			.append(header);
		
		for(Errors it : Errors.values()) {
			sb.append("|<div class=\"response-col_status\">")
				.append(it.status)
				.append("</div>")
				.append("|<div class=\"markdown\">")
				.append(it.getMessage())
				.append("</div>")
				.append("|\r\n");
		}
		
		sb.append(tail);
		
		return sb.toString();
	}
}
