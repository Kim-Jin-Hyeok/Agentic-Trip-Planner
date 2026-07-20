package com.tripagent.common.config;

import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Profiles;

public class ProductionEnvironmentValidator implements EnvironmentPostProcessor {

    private static final List<String> REQUIRED_ENVIRONMENT_VARIABLES = List.of(
            "DB_URL",
            "DB_USERNAME",
            "DB_PASSWORD",
            "AUTH_JWT_SECRET",
            "AI_OPENAI_API_KEY",
            "AI_OPENAI_MODEL",
            "KAKAO_LOCAL_REST_API_KEY"
    );

    @Override
    public void postProcessEnvironment(
            ConfigurableEnvironment environment,
            SpringApplication application
    ) {
        if (!environment.acceptsProfiles(Profiles.of("prod"))) {
            return;
        }

        List<String> missingVariables = REQUIRED_ENVIRONMENT_VARIABLES.stream()
                .filter(variableName -> isBlank(environment.getProperty(variableName)))
                .toList();
        if (!missingVariables.isEmpty()) {
            throw new IllegalStateException(
                    "Missing required production environment variables: "
                            + String.join(", ", missingVariables)
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
