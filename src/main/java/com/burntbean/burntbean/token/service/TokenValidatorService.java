package com.burntbean.burntbean.token.service;


import com.burntbean.burntbean.token.model.BurntbeanJwt;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenValidatorService {

    private final TokenService tokenService;

    public boolean isNotExistToken(String accessJws, String refreshJws, HttpServletResponse response) throws IOException {
        if(accessJws == null && refreshJws != null) {
            log.info("Access token is null but refresh token is not null ");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }else if (accessJws == null) {
            log.debug("Access token and Refresh token is null");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return true;
        }
        return false;
    }

    public boolean isInvalidAccessToken(BurntbeanJwt accessToken, HttpServletResponse response) throws IOException {
        if (accessToken != null && accessToken.getIsExpired()) {
            log.debug("Access token is expired");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return true;
        }else if (accessToken == null) {
            log.debug("Access token is null");
            //response.setStatus(HttpStatus.FORBIDDEN.value());
            return true;
        }
        return false;
    }

    public boolean isNotTokenExistNick(String accessJws, HttpServletResponse response) throws IOException {
        if (!tokenService.isNickExistFromToken(accessJws)) {
            log.info("access 201 response ");
            response.setStatus(HttpStatus.CREATED.value());
            return true;
        }
        return false;
    }

}