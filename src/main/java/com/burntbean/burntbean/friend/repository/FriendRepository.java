package com.burntbean.burntbean.friend.repository;

import com.burntbean.burntbean.friend.model.entity.FriendEntity;
import com.burntbean.burntbean.room.model.entity.RoomMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<FriendEntity,Long> {
    @Query("SELECT f FROM FriendEntity f JOIN FETCH f.friend WHERE f.me.id = :memberId")
    Optional<List<FriendEntity>> findListWithFriendByMemberId(@Param("memberId") Long memberId);

}
