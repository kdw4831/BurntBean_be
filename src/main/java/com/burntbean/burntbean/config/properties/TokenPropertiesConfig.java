package com.burntbean.burntbean.config.properties;

import com.burntbean.burntbean.token.model.TokenProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "token")
@Configuration
@Data
public class TokenPropertiesConfig {
    private TokenProperty accessToken;
    private TokenProperty refreshToken;
}
