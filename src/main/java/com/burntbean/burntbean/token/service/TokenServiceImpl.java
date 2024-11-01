package com.burntbean.burntbean.token.service;


import com.burntbean.burntbean.config.properties.TokenPropertiesConfig;
import com.burntbean.burntbean.security.authentication.SecurityContextManager;
import com.burntbean.burntbean.token.model.BurntbeanJwt;
import com.burntbean.burntbean.token.repository.RefreshTokenRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {
    private final TokenPropertiesConfig tokenPropertiesConfig;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityContextManager securityContextManager;

    private Integer accessExpiration;
    private Integer refreshExpiration;



    @PostConstruct
    public void init(){
        refreshExpiration= tokenPropertiesConfig.getRefreshToken().getExpiration();
        accessExpiration = tokenPropertiesConfig.getAccessToken().getExpiration();
    }

    @Override
    public void addCookieWithNewToken(HttpServletResponse response, String accessJws) {
        log.debug("newAccessJws: {}", accessJws);
        Cookie accessCookie = new Cookie("access-token", accessJws);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(refreshExpiration);
        response.addCookie(accessCookie);


    }

    @Override
    public void createTokenAndAddCookie(Long memberId, HttpServletResponse response, boolean isNickExist) {
        String accessJws = jwtService.createAccessTokenFromMemberId(memberId,isNickExist);
        log.debug("AccessJws: {}", accessJws);
        Cookie accessCookie = new Cookie("access-token", accessJws);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(accessExpiration);
        response.addCookie(accessCookie);



        String refreshJws = jwtService.createRefreshTokenFromMemberId(memberId);
        log.debug("refreshJws: {}", refreshJws);
        Cookie refreshCookie = new Cookie("refresh-token", refreshJws);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        accessCookie.setMaxAge(refreshExpiration);
        response.addCookie(refreshCookie);

    }

    @Override
    public boolean isNickExistFromToken(String accessJws) {
        boolean isNickExist = false;
        BurntbeanJwt jwt = BurntbeanJwt.fromJwt(accessJws,tokenPropertiesConfig.getAccessToken().getSecret());
        if(jwt != null) {
            isNickExist = jwt.getIsNickExist();
        }
        return isNickExist;
    }


    @Override
    public BurntbeanJwt validateAccessTokenAndToMakeObjectJwt(String accessJws) {
        return BurntbeanJwt.fromJwt(accessJws, tokenPropertiesConfig.getAccessToken().getSecret());
    }

    @Transactional
    @Override
    public boolean deleteRefreshTokenWithContextHolderFromMemberId() {
        Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());
        int result=refreshTokenRepository.deleteByMemberId(memberId);
        if(result>0) {
            // SecurityContextHolder 비우기
            SecurityContextHolder.clearContext();
            return true;
        }else{
            return false;
        }


    }
}