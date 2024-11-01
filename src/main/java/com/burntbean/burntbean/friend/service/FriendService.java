package com.burntbean.burntbean.friend.service;

import com.burntbean.burntbean.friend.model.dto.FriendDto;
import com.burntbean.burntbean.friend.model.entity.FriendEntity;
import com.burntbean.burntbean.friend.model.entity.FriendRequestEntity;
import com.burntbean.burntbean.member.model.dto.MemberDto;
import com.burntbean.burntbean.room.model.dto.RoomDto;

import java.util.List;

public interface FriendService {
    boolean addMemberInRequestFriend(Long toMemberId, String nick);
    List<MemberDto> getRequestFriendToMe();
    boolean deleteRequestAndJoinFriend(Long toMemberId);
    List<MemberDto> getFriends();
    boolean deleteFriendRequest(Long toMemberId);
}
