package com.bite.forum.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * Swagger配置类
 *
 * @Author ⽐特就业课
 */
// 配置类

// 开启Springfox-Swagger
//@EnableOpenApi
@Configuration
public class SwaggerConfig {
    /**
     * Springfox-Swagger基本配置
     * @return
     */
//    @Bean
//    public Docket createApi() {
//        Docket docket = new Docket(DocumentationType.OAS_30)
//                .apiInfo(apiInfo())
//                .select()
//
//                .apis(RequestHandlerSelectors.basePackage("com.bitejiuyeke.forum.controller"))
//                .paths(PathSelectors.any())
//                .build();
//        return docket;
//    }
    // 配置API基本信息
    @Bean
    public OpenAPI openAPI() {
        OpenAPI apiInfo = new OpenAPI()
                .info(new Info().title("Bit论坛系统API")
                .description("Bit论坛系统前后端分离API测试")
                .contact(new Contact())
                .version("v1"));
        return apiInfo;
    }
    /**
     * 解决SpringBoot 6.0以上与Swagger 3.0.0 不兼容的问题
     * 复制即可
     **/
//    @Bean
//    public WebMvcEndpointHandlerMapping
//    webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
//                                     ServletEndpointsSupplier servletEndpointsSupplier,
//                                     ControllerEndpointsSupplier controllerEndpointsSupplier,
//                                     EndpointMediaTypes endpointMediaTypes,
//                                     CorsEndpointProperties corsProperties,
//                                     WebEndpointProperties webEndpointProperties,
//                                     Environment environment) {
//        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
//        Collection<ExposableWebEndpoint> webEndpoints =
//                webEndpointsSupplier.getEndpoints();
//        allEndpoints.addAll(webEndpoints);
//        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
//        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
//        String basePath = webEndpointProperties.getBasePath();
//        EndpointMapping endpointMapping = new EndpointMapping(basePath); boolean shouldRegisterLinksMapping =
//                this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
//        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints,
//                endpointMediaTypes,
//                corsProperties.toCorsConfiguration(), new
//                EndpointLinksResolver(allEndpoints, basePath),
//                shouldRegisterLinksMapping, null);
//    }
//    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
//        return webEndpointProperties.getDiscovery().isEnabled() &&
//                (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
//    }
}
//package com.bite.forum.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Swagger配置类
// *
// * @Author 比特就业课
// */
//@Configuration
//public class SwaggerConfig {
//
//    /**
//     * 配置API基本信息
//     * @return OpenAPI 对象
//     */
//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Bit论坛系统API")
//                        .description("Bit论坛系统前后端分离API测试")
//                        .contact(new Contact()
//                                .name("支持团队")
//                                .email("support@example.com"))
//                        .version("v1")); // 这里设置了版本号
//    }
//}