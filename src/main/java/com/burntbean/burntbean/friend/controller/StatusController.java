package com.burntbean.burntbean.friend.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class StatusController {

    private final SimpMessagingTemplate messagingTemplate;

    public StatusController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void updateUserStatus(Long userId, String status) {
        String statusMessage = String.format("{\"userId\": \"%s\", \"status\": \"%s\"}", userId, status);
        messagingTemplate.convertAndSend("/status/updates", statusMessage);
    }
}
