package com.burntbean.burntbean.message.service;


import com.burntbean.burntbean.config.properties.TokenPropertiesConfig;
import com.burntbean.burntbean.exception.ResourceNotFoundException;
import com.burntbean.burntbean.member.model.dto.MemberDto;
import com.burntbean.burntbean.member.model.entity.MemberEntity;
import com.burntbean.burntbean.member.repository.MemberRepository;
import com.burntbean.burntbean.message.model.dto.MessageDto;
import com.burntbean.burntbean.message.model.entity.MessageEntity;
import com.burntbean.burntbean.message.repository.MessageRepository;
import com.burntbean.burntbean.room.repository.RoomRepository;
import com.burntbean.burntbean.security.authentication.SecurityContextManager;
import com.burntbean.burntbean.token.model.BurntbeanJwt;
import com.burntbean.burntbean.token.repository.RefreshTokenRepository;
import com.burntbean.burntbean.token.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
@Service
public class MessegeServiceImpl implements MessageService{


    private final MessageRepository messageRepository;

    @Override
    public List<MessageDto> getMessageList(Long chatId) {
        List<MessageEntity> entities= messageRepository.findByRoomIdOrderByCreateAtAsc(chatId).orElseThrow(
                () -> new ResourceNotFoundException("Not found message")
        );
        return entities.stream()
                .map(MessageDto::toDto)
                .toList();
    }

    @Override
    public MessageDto messageSave(MessageDto messageDto, Long memberId, Long chatId) {

        MessageEntity entity=messageRepository.save(MessageEntity.toEntity(messageDto,memberId,chatId));
        return   MessageDto.toDto(entity);

    }


}
