package com.tripagent.auth.support;

import com.tripagent.auth.service.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class OptionalLoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final BearerTokenExtractor bearerTokenExtractor;

    public OptionalLoginMemberIdArgumentResolver(
            JwtTokenProvider jwtTokenProvider,
            BearerTokenExtractor bearerTokenExtractor
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.bearerTokenExtractor = bearerTokenExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OptionalLoginMemberId.class)
                && Long.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = bearerTokenExtractor.extractOrNull(request);
        if (accessToken == null) {
            return null;
        }

        return jwtTokenProvider.getMemberId(accessToken);
    }
}
