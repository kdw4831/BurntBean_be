package com.burntbean.burntbean.config;

import com.burntbean.burntbean.member.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private final UserService userService;

    public WebSocketEventListener(UserService userService) {
        this.userService = userService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        String userId = event.getUser().getName();
        userService.restoreUserStatus(Long.parseLong(userId)); // WebSocket 연결 시 ONLINE
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String userId = event.getUser().getName();
        userService.userLoggedOut(Long.parseLong(userId)); // WebSocket 연결 종료 시 OFFLINE
    }
}
