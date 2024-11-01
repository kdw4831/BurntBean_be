package com.burntbean.burntbean.room.service;

import com.burntbean.burntbean.room.model.dto.RoomDto;

import java.util.List;

public interface RoomService {
    boolean createRoom(RoomDto roomDto);
    RoomDto getDmRoom(Long friendId);
    List<RoomDto> getGroupRoomList();
    List<RoomDto> getDmRoomList();
    RoomDto getRoomDetails(Long roomId);
    boolean addMemberInRequestGroup(Long roomId, String Nick);
    List<RoomDto> getRequestRoomToMe();
    boolean deleteRequestAndJoinRoom(Long roomId);
    boolean deleteRoomRequest(Long roomId);
}
