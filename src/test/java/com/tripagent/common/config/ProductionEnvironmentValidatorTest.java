package com.tripagent.common.config;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.mock.env.MockEnvironment;

class ProductionEnvironmentValidatorTest {

    private final ProductionEnvironmentValidator validator = new ProductionEnvironmentValidator();
    private final SpringApplication application = new SpringApplication();

    @Test
    void skipsValidationOutsideProductionProfile() {
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles("openai");

        assertThatCode(() -> validator.postProcessEnvironment(environment, application))
                .doesNotThrowAnyException();
    }

    @Test
    void acceptsProductionEnvironmentWhenAllRequiredVariablesExist() {
        MockEnvironment environment = productionEnvironment();
        environment
                .withProperty("DB_URL", "jdbc:mysql://database/trip_planner")
                .withProperty("DB_USERNAME", "trip")
                .withProperty("DB_PASSWORD", "database-secret")
                .withProperty("AUTH_JWT_SECRET", "jwt-secret")
                .withProperty("AI_OPENAI_API_KEY", "openai-key")
                .withProperty("AI_OPENAI_MODEL", "openai-model")
                .withProperty("KAKAO_LOCAL_REST_API_KEY", "kakao-key");

        assertThatCode(() -> validator.postProcessEnvironment(environment, application))
                .doesNotThrowAnyException();
    }

    @Test
    void rejectsProductionEnvironmentAndListsOnlyMissingVariableNames() {
        MockEnvironment environment = productionEnvironment();
        environment
                .withProperty("DB_URL", "jdbc:mysql://database/trip_planner")
                .withProperty("DB_USERNAME", "trip")
                .withProperty("DB_PASSWORD", " ")
                .withProperty("AUTH_JWT_SECRET", "jwt-secret")
                .withProperty("AI_OPENAI_API_KEY", "openai-key")
                .withProperty("AI_OPENAI_MODEL", "openai-model");

        assertThatThrownBy(() -> validator.postProcessEnvironment(environment, application))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Missing required production environment variables: "
                                + "DB_PASSWORD, KAKAO_LOCAL_REST_API_KEY"
                )
                .hasMessageNotContaining("jwt-secret")
                .hasMessageNotContaining("openai-key");
    }

    @Test
    void registeredValidatorStopsProductionApplicationBeforeContextCreation() {
        SpringApplication productionApplication = new SpringApplication(Object.class);
        productionApplication.setAdditionalProfiles("prod");
        productionApplication.setWebApplicationType(WebApplicationType.NONE);

        assertThatThrownBy(() -> productionApplication.run(
                "--spring.main.banner-mode=off",
                "--logging.level.root=off",
                "--DB_URL=",
                "--DB_USERNAME=",
                "--DB_PASSWORD=",
                "--AUTH_JWT_SECRET=",
                "--AI_OPENAI_API_KEY=",
                "--AI_OPENAI_MODEL=",
                "--KAKAO_LOCAL_REST_API_KEY="
        ))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Missing required production environment variables")
                .hasMessageContaining("DB_URL")
                .hasMessageContaining("KAKAO_LOCAL_REST_API_KEY");
    }

    private MockEnvironment productionEnvironment() {
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles("prod");
        return environment;
    }
}
