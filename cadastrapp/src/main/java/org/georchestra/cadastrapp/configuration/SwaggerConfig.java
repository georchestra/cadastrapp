package org.georchestra.cadastrapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.PathProvider;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;


@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {


    @Bean
    public Docket api() {

       
        return new Docket(DocumentationType.SWAGGER_2)
        .pathProvider(new PathProvider() {

            // remvove duplicate cadastrapp entry in operation path
            @Override
            public String getOperationPath(String operationPath) {
                return operationPath.replace("cadastrapp/", "");
            }

            @Override
            public String getResourceListingPath(String groupName, String apiDeclaration) {
                return null;
            }
        })
        .select()
        .apis(RequestHandlerSelectors.any())
        .build()
        .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("geOrchestra Cadastrapp API").description("API to access to Majic and EDIGEO information").version("1.10")
                .termsOfServiceUrl("https://docs.georchestra.org/cadastrapp/latest/")
                .license("GNU General Public License v3.0")
                .licenseUrl("https://github.com/georchestra/georchestra/blob/master/LICENSE.txt")
                .build();
    }

}
