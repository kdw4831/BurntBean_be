package com.burntbean.burntbean.security.authentication;


import com.burntbean.burntbean.token.model.BurntbeanJwt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SecurityManagerImpl implements SecurityContextManager {

    @Override
    public void setUpSecurityContext(BurntbeanJwt accessToken, HttpServletRequest request) {
        String memberId = accessToken.getSubject();
        log.info("memberId:{}", memberId);
        UserDetails userDetails = new User(memberId, "", List.of());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
    @Override
    public String getAuthenticatedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null; // 인증된 사용자가 없는 경우
    }
}
