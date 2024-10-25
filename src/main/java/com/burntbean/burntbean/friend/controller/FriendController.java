package com.burntbean.burntbean.friend.controller;


import com.burntbean.burntbean.friend.service.FriendService;
import com.burntbean.burntbean.member.model.dto.MemberDto;
import com.burntbean.burntbean.room.model.dto.RoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/friend")
public class FriendController {
    private final FriendService friendService;

    /**
     * friend list 불러오기
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<MemberDto>> getFriends(){
        List<MemberDto> friends = friendService.getFriends();
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/join/request")
    public ResponseEntity<Boolean> joinRequest(@RequestParam Long FromMemberId, @RequestParam String nick) {
        boolean isSuccess=friendService.addMemberInRequestFriend(FromMemberId,nick);
        return ResponseEntity.ok(isSuccess);
    }

    @GetMapping("/request/list")
    public ResponseEntity<List<MemberDto>> getRequestList() {
        List<MemberDto> dtoList = friendService.getRequestFriendToMe();
        return ResponseEntity.ok(dtoList);
    }


    @GetMapping("/request/accept")
    public ResponseEntity<Boolean> acceptRequest(@RequestParam Long toMemberId) {
        boolean isSuccess =  friendService.deleteRequestAndJoinFriend(toMemberId);

        return ResponseEntity.ok(isSuccess);
    }

}
