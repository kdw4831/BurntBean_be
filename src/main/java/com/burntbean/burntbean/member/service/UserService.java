package com.burntbean.burntbean.member.service;

import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    public void checkAndJoinUser(String email,String profile,String nick, HttpServletResponse response);
    public void userLoggedOut(Long userId);
    public void restoreUserStatus(Long userId);
}
