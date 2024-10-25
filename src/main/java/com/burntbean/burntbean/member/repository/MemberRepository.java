package com.burntbean.burntbean.member.repository;

import com.burntbean.burntbean.member.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByEmail(String email);

    Optional<MemberEntity> findByNick(String nick);

    @Query("SELECT m FROM MemberEntity m " +
            "LEFT JOIN FETCH m.roomList mg " +
            "WHERE m.nick LIKE %:nick% " +
            "AND m.id NOT IN (" +
            "    SELECT mr.member.id FROM RoomMemberEntity mr " +
            "    WHERE mr.room.id = :roomId" +
            ")")
    Optional<List<MemberEntity>> findByNickContainingExcludingRoom(@Param("nick") String nick, @Param("roomId") Long roomId);

    @Query("SELECT m FROM MemberEntity m " +
            "LEFT JOIN FETCH m.roomList rm " +
            "WHERE m.nick LIKE %:nick% " +
            "AND m.id NOT IN (" +
            "    SELECT rm.member.id FROM RoomMemberEntity rm " +
            "    WHERE rm.room.id = :roomId" +
            ") " +
            "AND m.id NOT IN (" +
            "    SELECT rr.member.id FROM RoomRequestEntity rr " +
            "    WHERE rr.room.id = :roomId" +
            ")")
    Optional<List<MemberEntity>> findMemberListExcludingGroupMemberAndAlreadyRequestedList(@Param("nick") String nick, @Param("roomId") Long roomId);



}
