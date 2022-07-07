package com.cracker.cracker.auth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class AppProperties {
    @Value("${app.auth.tokenExpiry}")
    private long tokenExpiry;
    @Value("${app.auth.refreshTokenExpiry}")
    private long refreshTokenExpiry;
}
