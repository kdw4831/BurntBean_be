package com.burntbean.burntbean.security.authentication;


import com.burntbean.burntbean.token.model.BurntbeanJwt;
import jakarta.servlet.http.HttpServletRequest;


public interface   SecurityContextManager {

    public void setUpSecurityContext(BurntbeanJwt accessToken, HttpServletRequest request);

    public String getAuthenticatedUserName();

}