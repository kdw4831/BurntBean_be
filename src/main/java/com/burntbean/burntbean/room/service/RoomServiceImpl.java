package com.burntbean.burntbean.room.service;

import com.burntbean.burntbean.common.enums.Rtype;
import com.burntbean.burntbean.exception.CustomServiceException;
import com.burntbean.burntbean.exception.ResourceNotFoundException;
import com.burntbean.burntbean.member.model.dto.MemberDto;
import com.burntbean.burntbean.member.model.entity.MemberEntity;
import com.burntbean.burntbean.member.repository.MemberRepository;
import com.burntbean.burntbean.room.model.dto.RoomDto;
import com.burntbean.burntbean.room.model.entity.RoomEntity;
import com.burntbean.burntbean.room.model.entity.RoomMemberEntity;
import com.burntbean.burntbean.room.model.entity.RoomRequestEntity;
import com.burntbean.burntbean.room.repository.RoomMemberRepository;
import com.burntbean.burntbean.room.repository.RoomRepository;
import com.burntbean.burntbean.room.repository.RoomRequestRepository;
import com.burntbean.burntbean.security.authentication.SecurityContextManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final SecurityContextManager securityContextManager;
    private final MemberRepository memberRepository;
    private final RoomRequestRepository roomRequestRepository;

    /**
     * 방 생성
     * @param roomDto
     * @return
     */
    @Transactional
    @Override
    public boolean createRoom(RoomDto roomDto) {
        try {

            log.info("Creating room: {}", roomDto);
            roomDto.setTotal(1);
            Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());
            roomDto.setHostId(memberId);
            roomDto.setRtype(Rtype.group);
            log.info("hostId: {}", roomDto.getHostId());
            RoomEntity roomEntity = RoomEntity.toEntity(roomDto);
            RoomEntity savedRoomEntity = roomRepository.save(roomEntity);

            //RoomMemberEntity 생성 및 저장하기  (채팅 room을 의미하기도 하지 )
            RoomMemberEntity roomMemberEntity = RoomMemberEntity.ToRoomMemberEntity(savedRoomEntity,memberId);
            roomMemberRepository.save(roomMemberEntity);

            return true;
        }catch (DataAccessException e){
            //데이터베이스 관련 예외 처리
            log.error("Database error creating room: {}", e.getMessage());
            return false;
        }catch (Exception e){
            log.error("createRoom occured {}", e.getMessage());
            return false;
        }
    }

    @Transactional
    @Override
    public RoomDto getDmRoom(Long friendId) {

        Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());
        Optional<RoomEntity> room=roomRepository.existsRoomByFriendIdAndMyId(friendId,memberId);


        if(room.isPresent()){
         return RoomDto.toDto(room.get());
        }else{
            //uuid로 groupName 만들기 => 화상회의 할 때 그룹이름이 필수
            String randomGroupName = UUID.randomUUID().toString();

            //RoomEntity를 생성
            RoomEntity dmRoom = new RoomEntity();
            dmRoom.setRtype(Rtype.dm);
            dmRoom.setTotal(2);
            dmRoom.setGroupName(randomGroupName);
            RoomEntity savedRoomEntity = roomRepository.save(dmRoom);

            //나의 아이디를 roomMember에 저장
            RoomMemberEntity roomMemberEntity = RoomMemberEntity.ToRoomMemberEntity(savedRoomEntity,memberId);
            roomMemberRepository.save(roomMemberEntity);

            //친구의 아이디를 roomMember에 저장
            RoomMemberEntity roomMemberEntity2 = RoomMemberEntity.ToRoomMemberEntity(savedRoomEntity,friendId);
            roomMemberRepository.save(roomMemberEntity2);



            return RoomDto.toDto(savedRoomEntity);

        }

    }

    /**
     *  본인의 room List 가져오기
     * @return
     */
    @Override
    public List<RoomDto> getGroupRoomList() {
        try {

            Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());
            List<RoomMemberEntity> roomMembers = roomMemberRepository.findListWithRoomGroupByMemberId(memberId)
                    .orElseThrow(() -> new ResourceNotFoundException("memberId: " + memberId + "not found room"));

            return roomMembers.stream()
                    .map(roomMemberEnity -> RoomDto.toDto(roomMemberEnity.getRoom()))
                    .collect(Collectors.toList());
        }catch (DataAccessException e) {
                // 데이터베이스 관련 예외 처리
                log.error("Database error getListGroup: {} ", e.getMessage());
                throw new CustomServiceException("Failed to get group list due to database error", e);
        } catch (Exception e) {
            log.error("getListGroup occurred {}", e.getMessage());
            throw new CustomServiceException("Failed to get group list", e);
        }
    }

    @Override
    public List<RoomDto> getDmRoomList() {
        try {

            Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());
            List<RoomMemberEntity> roomMembers = roomMemberRepository.findListWithRoomDmByMemberId(memberId)
                    .orElseThrow(() -> new ResourceNotFoundException("memberId: " + memberId + "not found room"));

            return roomMembers.stream()
                    .map(roomMemberEnity -> RoomDto.toDto(roomMemberEnity.getRoom()))
                    .collect(Collectors.toList());
        }catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리
            log.error("Database error getListGroup: {} ", e.getMessage());
            throw new CustomServiceException("Failed to get group list due to database error", e);
        } catch (Exception e) {
            log.error("getListGroup occurred {}", e.getMessage());
            throw new CustomServiceException("Failed to get group list", e);
        }
    }

    @Override
    public RoomDto getRoomDetails(Long roomId) {
        try {
            RoomEntity roomEntity = roomRepository.findRoomWithMembers(roomId)
                    .orElseThrow(() -> new ResourceNotFoundException("room Id:" + roomId + " not found"));
            RoomDto roomDto = RoomDto.toDto(roomEntity);
            List<MemberDto> memberList = roomEntity.getRoomMembers().stream()
                    .map(RoomMemberEntity::getMember)
                    .map(MemberDto::toDto)
                    .toList();
            roomDto.setMembers(memberList);
            return roomDto;
        } catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리
            log.error("Database error GroupDetail: {} ", e.getMessage());
            throw new CustomServiceException("Failed to get group detail due to database error", e);
        } catch (Exception e) {
            log.error("groupDetails occurred {}", e.getMessage());
            throw new CustomServiceException("Failed to get group Detail", e);
        }
    }

    /**
     *  그룹 request를 받았을 때 room_request 테이블에 추가
     * @param roomId
     * @param nick
     * @return
     */
    @Override
    public boolean addMemberInRequestGroup(Long roomId, String nick) {
        try{
            MemberEntity memberEntity = memberRepository.findByNick(nick).orElseThrow(()-> new ResourceNotFoundException("nick: " + nick + " not found"));

            if(roomRequestRepository.existsByRoomIdAndMemberId(roomId, memberEntity.getId())) {
                // 이미 존재하는 요청이 있는경우는 false를 반환한다.
                return false;
            }
            RoomEntity roomEntity = new RoomEntity();
            roomEntity.setId(roomId);
            RoomRequestEntity roomRequestEntity = new RoomRequestEntity();
            roomRequestEntity.setMember(memberEntity);
            roomRequestEntity.setRoom(roomEntity);
            RoomRequestEntity savedRoomRequestEntity = roomRequestRepository.save(roomRequestEntity);

            // id 값이 0보다 크면 true (id 중 0을 포함한 음수는 존재하지 않으니까..)
            return savedRoomRequestEntity.getId()>0;

        }catch (DataAccessException e) {
            log.error("Database error addMemberInRequestGroup: {} ", e.getMessage());
            throw new CustomServiceException("Failed to addMemberInRequestGroup due to database error", e);
        } catch (Exception e) {
            log.error("addMemberInRequestGroup occurred {}", e.getMessage());
            throw new CustomServiceException("Failed to addMemberInRequestGroup", e);
        }


    }

    @Override
    public List<RoomDto> getRequestRoomToMe() {
        try {

            Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());
            List<RoomRequestEntity> entityList = roomRequestRepository.requestRoomList(memberId)
                    .orElseThrow(() -> new ResourceNotFoundException("not found List"));

            return entityList.stream()
                    .map(roomRequestEntity -> RoomDto.toDto(roomRequestEntity.getRoom()))
                    .toList();
        }catch (DataAccessException e) {
            log.error("Database error getRequestGroupListToMe: {} ", e.getMessage());
            throw new CustomServiceException("Failed to getRequestGroupListToMe due to database error", e);
        } catch (Exception e) {
            log.error("getRequestGroupListToMe occurred {}", e.getMessage());
            throw new CustomServiceException("Failed to getRequestGroupListToMe", e);
        }
    }

    /**
     * group 요청 수락시 수행로직 요청 목록을 지우기
     * @param roomId
     * @return
     */
    @Transactional
    @Override
    public boolean deleteRequestAndJoinRoom(Long roomId) {
        boolean isSuccess = false;
        Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());

        // 특정 그룹 Id와 회원 ID를 기반으로 그룹 요청 entity를 삭제한다.
        roomRequestRepository.deleteByRoomRequestIdAndMemberId(roomId, memberId);

        // 룸 엔티티를 ID로 조회, 없으면 예외 발생
        RoomEntity roomEntity = roomRepository.findById(roomId)
                .orElseThrow(()-> new ResourceNotFoundException("No group found with id" + roomId));

        // 그룹의 총 인원 수 증가
        roomEntity.setTotal(roomEntity.getTotal()+1);

        // 회원 엔티티 생성 및 설정
        var memberEntity = new MemberEntity();
        memberEntity.setId(memberId);

        RoomMemberEntity roomMemberEntity= new RoomMemberEntity();
        roomMemberEntity.setRoom(roomEntity);
        roomMemberEntity.setMember(memberEntity);

        // RoomMember 저장
        RoomMemberEntity savedRoomMemberEntity= roomMemberRepository.save(roomMemberEntity);

        if(savedRoomMemberEntity.getId() !=null){
            isSuccess = true;
        }

        return isSuccess;
    }

    @Transactional
    @Override
    public boolean deleteRoomRequest(Long roomId) {
        boolean isSuccess = false;
        Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());
        // 특정 그룹 Id와 회원 ID를 기반으로 그룹 요청 entity를 삭제한다.
        int deleteCount=roomRequestRepository.deleteByRoomRequestIdAndMemberId(roomId, memberId);
        if(deleteCount>0){
            isSuccess = true;
        }
        return isSuccess;
    }
}
