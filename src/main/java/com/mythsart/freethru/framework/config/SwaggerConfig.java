package com.mythsart.freethru.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends CommonConfig {

    private final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);

    public static final String BASE_PACKAGE = "com.mythsart.freethru";

    public static final String API_VERSION = "dev";

    public static final String API_CONTACT_NAME = "kenhuey";

    public static final String API_CONTACT_URL = "freethru.mythsart.com";

    public static final String API_CONTACT_EMAIL = "kenhuey@qq.com";

    public static final String API_LICENSE = "MIT";

    public static final String API_LICENSE_URL = "https://github.com/Kenhuey/freethru/blob/main/LICENSE";

    public static final String PATH_CONSUMER = "/consumer";

    public static final String PATH_ADMIN = "/admin";

    public static final String PATH_COMMON = "/common";

    public static final String GROUP_NAME_CONSUMER = "0_consumer";

    public static final String GROUP_NAME_ADMIN = "1_admin";

    public static final String GROUP_NAME_COMMON = "2_common";

    public static final String TAG_NAME_CONSUMER_AUTH = "consumer_auth";

    public static final String TAG_NAME_ADMIN_AUTH = "admin_auth";

    public static final String TAG_NAME_ADMIN_MANAGE = "admin_manage";

    public static final String TAG_NAME_CONSUMER_DELIVERY = "consumer_delivery";

    @Bean
    public Docket createCommonRestApi() {
        this.logger.info("Common swagger-document loading.");
        final ParameterBuilder parameterBuilder = new ParameterBuilder();
        final List<Parameter> parameterList = new ArrayList<>();
        parameterList.add(parameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(GROUP_NAME_COMMON)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.regex(PATH_COMMON + ".*"))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("common apis")
                        .description("common response")
                        .version(API_VERSION)
                        .contact(new Contact(API_CONTACT_NAME, API_CONTACT_URL, API_CONTACT_EMAIL))
                        .license(API_LICENSE)
                        .licenseUrl(API_LICENSE_URL)
                        .build());
    }

    @Bean
    public Docket createAdminRestApi() {
        this.logger.info("Admin swagger-document loading.");
        final ParameterBuilder parameterBuilder = new ParameterBuilder();
        final List<Parameter> parameterList = new ArrayList<>();
        parameterList.add(parameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(GROUP_NAME_ADMIN)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.regex(PATH_ADMIN + ".*"))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("admin apis")
                        .description("for admin")
                        .version(API_VERSION)
                        .contact(new Contact(API_CONTACT_NAME, API_CONTACT_URL, API_CONTACT_EMAIL))
                        .license(API_LICENSE)
                        .licenseUrl(API_LICENSE_URL)
                        .build())
                .securitySchemes(this.securitySchemes(HEADER_TOKEN_NAME)) // ???
                .securityContexts(this.securityContexts(HEADER_TOKEN_NAME)) // ???
                .tags(new Tag(TAG_NAME_ADMIN_AUTH, "admin authorization api"))
                .tags(new Tag(TAG_NAME_ADMIN_MANAGE, "admin management api"));
    }

    @Bean
    public Docket createFrontRestApi() {
        this.logger.info("Consumer swagger-document loading.");
        final ParameterBuilder parameterBuilder = new ParameterBuilder();
        final List<Parameter> parameterList = new ArrayList<>();
        parameterList.add(parameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(GROUP_NAME_CONSUMER)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.regex(PATH_CONSUMER + ".*"))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("front apis")
                        .description("for user")
                        .version(API_VERSION)
                        .contact(new Contact(API_CONTACT_NAME, API_CONTACT_URL, API_CONTACT_EMAIL))
                        .license(API_LICENSE)
                        .licenseUrl(API_LICENSE_URL)
                        .build())
                .securitySchemes(this.securitySchemes(HEADER_TOKEN_NAME))
                .securityContexts(this.securityContexts(HEADER_TOKEN_NAME))
                .tags(new Tag(TAG_NAME_CONSUMER_AUTH, "wechat user authorization api"))
                .tags(new Tag(TAG_NAME_CONSUMER_DELIVERY, "consumer delivery api"));
    }

    private List<ApiKey> securitySchemes(final String tokenName) {
        final List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey(tokenName, tokenName, "header"));
        return apiKeys;
    }

    private List<SecurityReference> defaultAuth(final String tokenName) {
        final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        final List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference(tokenName, authorizationScopes));
        return securityReferences;
    }

    private List<SecurityContext> securityContexts(final String tokenName) {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(SecurityContext.builder()
                .securityReferences(defaultAuth(tokenName))
                .forPaths(PathSelectors.regex("^(?!auth).*$")).build());
        return securityContexts;
    }

}
