package com.tripagent.common.config;

import com.tripagent.auth.support.LoginMemberIdArgumentResolver;
import com.tripagent.auth.support.OptionalLoginMemberIdArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberIdArgumentResolver loginMemberIdArgumentResolver;
    private final OptionalLoginMemberIdArgumentResolver optionalLoginMemberIdArgumentResolver;

    public WebConfig(
            LoginMemberIdArgumentResolver loginMemberIdArgumentResolver,
            OptionalLoginMemberIdArgumentResolver optionalLoginMemberIdArgumentResolver
    ) {
        this.loginMemberIdArgumentResolver = loginMemberIdArgumentResolver;
        this.optionalLoginMemberIdArgumentResolver = optionalLoginMemberIdArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberIdArgumentResolver);
        resolvers.add(optionalLoginMemberIdArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}
