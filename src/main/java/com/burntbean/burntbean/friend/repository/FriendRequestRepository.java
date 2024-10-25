package com.burntbean.burntbean.friend.repository;

import com.burntbean.burntbean.friend.model.entity.FriendRequestEntity;
import com.burntbean.burntbean.room.model.entity.RoomRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity,Long> {
    @Query("SELECT f FROM FriendRequestEntity f JOIN FETCH f.fromMember JOIN FETCH f.toMember where f.fromMember.id = :memberId")
    Optional<List<FriendRequestEntity>> requestFriendList(Long memberId);

    @Modifying
    @Query("DELETE FROM FriendRequestEntity f WHERE f.fromMember.id = :fromMemberId AND f.toMember.id = :toMemberId")
    void deleteByFriendRequestIdAndMemberId(@Param("fromMemberId") Long fromMemberId, @Param("toMemberId") Long toMemberId);

    boolean existsByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
}
