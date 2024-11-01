package com.burntbean.burntbean.room.controller;

import com.burntbean.burntbean.room.model.dto.RoomDto;
import com.burntbean.burntbean.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;

    /**
     * @param dto
     * @return 성공하면 200 실패하면 400
     */
    @PostMapping("/create")
    public ResponseEntity<Boolean> createRoom(@RequestBody RoomDto dto) {
        boolean isSuccess = roomService.createRoom(dto);
        if (isSuccess) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    /**
     * room group list 불러오기
     * @return
     */
    @GetMapping("/list/group")
    public ResponseEntity<List<RoomDto>> getAllGroupRooms() {

        List<RoomDto> roomList = roomService.getGroupRoomList();
        return ResponseEntity.ok(roomList);

    }

    /**
     * room dm list 불러오기
     * @return
     */
    @GetMapping("/list/dm")
    public ResponseEntity<List<RoomDto>> getAllDmRooms() {

        List<RoomDto> roomList = roomService.getGroupRoomList();
        return ResponseEntity.ok(roomList);

    }

    @GetMapping("/detail/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId) {

        RoomDto roomDto = roomService.getRoomDetails(roomId);
        return ResponseEntity.ok().body(roomDto);
    }


    /**
     * 친구를 클릭시에 room이 존재하면 그대로 가져오고
     * 존재하지 않으면 친구와 나자신을 RoomMember에 저장하여
     * room을 가져온다.
     * @param friendId
     * @return roomDto
     */
    @GetMapping("/detail/dm")
    public ResponseEntity<RoomDto> getDmRooms(Long friendId) {
        RoomDto roomDto = roomService.getDmRoom(friendId);
        return ResponseEntity.ok().body(roomDto);
    }


    /**
     *
     * @param roomId
     * @param nick
     * @return 방 초대 요청을 보내는 컨트롤러
     */
    @GetMapping("/join/request")
    public ResponseEntity<Boolean> joinRequest(@RequestParam Long roomId,@RequestParam String nick) {
        boolean isSuccess=roomService.addMemberInRequestGroup(roomId,nick);
        return ResponseEntity.ok(isSuccess);
    }


    /**
     *
     * @return 유저가 받은 방 초대 요청 리스트
     */
    @GetMapping("/request/list")
    public ResponseEntity<List<RoomDto>> getRequestList() {
        List<RoomDto> dtoList = roomService.getRequestRoomToMe();
        return ResponseEntity.ok(dtoList);
    }

    /**
     *
     * @param roomId
     * @return 방 요청 초대 수락 시 보내는 요청
     */
    @GetMapping("/request/accept")
    public ResponseEntity<Boolean> acceptRequest(@RequestParam Long roomId) {
        boolean isSuccess =  roomService.deleteRequestAndJoinRoom(roomId);

        return ResponseEntity.ok(isSuccess);
    }

   @DeleteMapping("/request/reject")
    public ResponseEntity<Boolean> rejectRequest(@RequestParam Long roomId) {
        boolean isSuccess =  roomService.deleteRoomRequest(roomId);
        return ResponseEntity.ok(isSuccess);
   }


}
