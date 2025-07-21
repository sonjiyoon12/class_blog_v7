package com.tenco.blog._core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Swagger 기본 설정하는 클래스
 * API 문서의 제목, 설명 JWT 인증 설정
 */
// 스프링컨테이너에 new 호출 하려고
//@Component // 객체만 확인해서 단독적으로
@Configuration // 객체와 메서드 확인
public class OpenApiConfig {

    @Bean // IoC 대상
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                // API 기본 정보
                .info(new Info().title("Tenco Blog API")
                        .description("RESTful API ver 1.0")
                        .version("1.0"))
                .components(new Components().addSecuritySchemes(
                        "JWT",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                // 모든 API 에 JWT 인증 적용
                .addSecurityItem(new SecurityRequirement().addList("JWT"));
    }
}
