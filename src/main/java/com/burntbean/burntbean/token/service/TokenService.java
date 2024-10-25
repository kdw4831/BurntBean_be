package com.burntbean.burntbean.token.service;


import com.burntbean.burntbean.token.model.BurntbeanJwt;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {
    public void addCookieWithNewToken(HttpServletResponse response, String accessJws);
    public void createTokenAndAddCookie(Long memberId, HttpServletResponse response, boolean isExistNick);
    public boolean isNickExistFromToken(String accessJws);
    public BurntbeanJwt validateAccessTokenAndToMakeObjectJwt(String accessJws);
    public boolean deleteRefreshTokenWithContextHolderFromMemberId();
}
