package com.burntbean.burntbean.room.repository;

import com.burntbean.burntbean.room.model.entity.RoomRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRequestRepository extends JpaRepository<RoomRequestEntity,Long> {

    @Query("SELECT r FROM RoomRequestEntity r JOIN FETCH r.member JOIN FETCH r.room where r.member.id = :memberId")
    Optional<List<RoomRequestEntity>> requestRoomList(Long memberId);

    @Modifying
    @Query("DELETE FROM RoomRequestEntity r WHERE r.room.id = :roomId AND r.member.id = :memberId")
    void deleteByRoomRequestIdAndMemberId(@Param("roomId") Long roomId, @Param("memberId") Long memberId);

    boolean existsByRoomIdAndMemberId(Long groupId, Long memberId);
}


