package com.burntbean.burntbean.room.repository;

import com.burntbean.burntbean.room.model.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<RoomEntity,Long> {
    @Query("SELECT r FROM RoomEntity r JOIN FETCH r.roomMembers rm JOIN FETCH rm.member WHERE r.id = :roomId")
    Optional<RoomEntity> findRoomWithMembers(@Param("roomId") Long roomId);

    @Query("SELECT rm.room FROM RoomMemberEntity rm " +
            "JOIN rm.room r " +
            "WHERE rm.member.id = :myId " +
            "AND rm.room.id IN (SELECT rm2.room.id FROM RoomMemberEntity rm2 WHERE rm2.member.id = :friendId) " +
            "AND r.rtype = 'dm'")
    Optional<RoomEntity> existsRoomByFriendIdAndMyId(@Param("friendId")Long friendId, @Param("myId")Long myId);
}
