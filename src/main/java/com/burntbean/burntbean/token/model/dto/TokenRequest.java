package com.burntbean.burntbean.token.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRequest {
    @JsonProperty("idToken")
    private String idToken;

    @JsonProperty("access_jws")
    private String accessJws;

    @JsonProperty("refresh_jws")
    private String refreshJws;

}