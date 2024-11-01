package com.burntbean.burntbean.message.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/invite")
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/send/{userId}")
    public void sendInviteNotification(@PathVariable String userId, @RequestBody String message) {
        // 특정 사용자의 queue로 메시지 전송
        messagingTemplate.convertAndSend("/queue/notification/" + userId, message);
        System.out.println("Sent invite notification to " + userId);
    }

    @PostMapping("/send/friend/{userId}")
    public void sendFriendInviteNotification(@PathVariable String userId, @RequestBody String message) {
        // 특정 사용자의 queue로 메시지 전송
        messagingTemplate.convertAndSend("/queue/notification/friend/" + userId, message);
        System.out.println("Sent invite notification to " + userId);
    }
}
