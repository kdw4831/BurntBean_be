package com.burntbean.burntbean.message.service;

import com.burntbean.burntbean.member.model.dto.MemberDto;
import com.burntbean.burntbean.message.model.dto.MessageDto;

import java.util.List;

public interface MessageService {
    List<MessageDto> getMessageList (Long chatId);
    MessageDto messageSave(MessageDto messageDto, Long memberId, Long roomId);

}
