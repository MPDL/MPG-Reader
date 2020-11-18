package de.mpg.mpdl.reader.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
    @Value("${swagger.host}")
    private String swaggerHost;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("de.mpg.mpdl.reader.controller"))
                .paths(PathSelectors.any())
                .build()
                .host(swaggerHost);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MPDL Reader backend")
                .description("MPDL Reader Backend Rest Doc")
                .version("1.0")
                .build();
    }
}