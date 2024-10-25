package com.burntbean.burntbean.token.model;

import lombok.Data;

@Data
public class TokenProperty {
    String secret;
    Integer expiration;
}
