package com.ritish.ai_calling_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI bookingOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Ai Calling System - Booking APIU")

                .version("v1")
                .description("Apis for creatung, listing, cancelling and rescheduling bookings"));
    }

}
