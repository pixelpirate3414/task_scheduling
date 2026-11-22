package com.taskscheduller.api.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;

@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapperBuilderCustomizer jsonMapperCustomizer() {
          return builder -> builder
                  .enable(SerializationFeature.INDENT_OUTPUT)
                  .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                  .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
