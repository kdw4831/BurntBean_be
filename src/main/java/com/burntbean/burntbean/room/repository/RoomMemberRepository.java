package com.burntbean.burntbean.room.repository;

import com.burntbean.burntbean.room.model.entity.RoomMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMemberEntity,Long> {
    @Query("SELECT rm FROM RoomMemberEntity rm JOIN FETCH rm.room r WHERE rm.member.id = :memberId AND r.rtype = 'group'")
    Optional<List<RoomMemberEntity>> findListWithRoomGroupByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT rm FROM RoomMemberEntity rm JOIN FETCH rm.room r WHERE rm.member.id = :memberId AND r.rtype = 'dm'")
    Optional<List<RoomMemberEntity>> findListWithRoomDmByMemberId(@Param("memberId") Long memberId);


}
