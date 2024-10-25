package com.burntbean.burntbean.message.repository;

import com.burntbean.burntbean.message.model.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    Optional<List<MessageEntity>> findByRoomIdOrderByCreateAtAsc(Long roomId);
}
