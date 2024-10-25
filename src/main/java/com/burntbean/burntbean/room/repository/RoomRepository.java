package com.burntbean.burntbean.room.repository;

import com.burntbean.burntbean.room.model.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<RoomEntity,Long> {
    @Query("SELECT r FROM RoomEntity r JOIN FETCH r.roomMembers rm JOIN FETCH rm.member WHERE r.id = :roomId")
    Optional<RoomEntity> findRoomWithMembers(@Param("roomId") Long roomId);
}
