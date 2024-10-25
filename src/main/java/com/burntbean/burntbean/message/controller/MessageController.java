package com.burntbean.burntbean.message.controller;


import com.burntbean.burntbean.message.model.dto.MessageDto;
import com.burntbean.burntbean.message.service.MessageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@RequestMapping("/api/message")
@Slf4j
@Controller
public class MessageController {
    private final MessageService messageService;

    /**
     *
     * @param roomId 채팅방id
     * @return 채팅방의 id에 따른 대화내용
     */
    @ResponseBody
    @GetMapping("/{roomId}")
    public ResponseEntity<List<MessageDto>> getMessageList(@PathVariable Long roomId){
        return ResponseEntity.ok().body(messageService.getMessageList(roomId));
    }

    /**
     *
     * @param roomId
     * @param dto MessageDto
     * @param authentication
     * @return 채팅방에 소켓통신을 위한 메소드
     */
    @MessageMapping("/{roomId}")
    @SendTo("/room/{roomId}")
    public MessageDto sendMessage(@DestinationVariable("roomId") Long roomId, MessageDto dto, Authentication authentication) {

        Long memberId = Long.parseLong(authentication.getName());
        var result = messageService.messageSave(dto,memberId,roomId);
        result.setNick(dto.getNick());

        return result;
    }

}
