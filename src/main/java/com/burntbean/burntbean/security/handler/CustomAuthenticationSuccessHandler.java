package com.burntbean.burntbean.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> attributes = user.getAttributes();
        String email = (String) attributes.get("email");
        log.info("email is: {}",email);

        response.sendRedirect("https://be.burntbean.site/api/login/success");


    }
}
