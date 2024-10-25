package com.burntbean.burntbean.friend.service;

import com.burntbean.burntbean.exception.CustomServiceException;
import com.burntbean.burntbean.exception.ResourceNotFoundException;
import com.burntbean.burntbean.friend.model.dto.FriendDto;
import com.burntbean.burntbean.friend.model.entity.FriendEntity;
import com.burntbean.burntbean.friend.model.entity.FriendRequestEntity;
import com.burntbean.burntbean.friend.repository.FriendRepository;
import com.burntbean.burntbean.friend.repository.FriendRequestRepository;
import com.burntbean.burntbean.member.model.dto.MemberDto;
import com.burntbean.burntbean.member.model.entity.MemberEntity;
import com.burntbean.burntbean.member.repository.MemberRepository;
import com.burntbean.burntbean.security.authentication.SecurityContextManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FriendServiceImpl implements FriendService {
    private final SecurityContextManager securityContextManager;
    private final MemberRepository memberRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;


    //수정 필요
    @Override
    public boolean addMemberInRequestFriend(Long toMemberId, String nick) { 
        try{
            Long meId= Long.parseLong(securityContextManager.getAuthenticatedUserName());

            MemberEntity friend = memberRepository.findByNick(nick).orElseThrow(()-> new ResourceNotFoundException("nick: " + nick + " not found"));
            if(friendRequestRepository.existsByFromMemberIdAndToMemberId(friend.getId(),meId)){
                //이미 존재하는 요청이 있는 경우는 false를 반환
                return false;
            }
            MemberEntity me= new MemberEntity();
            me.setId(meId);
            FriendRequestEntity friendRequestEntity = new FriendRequestEntity();
            friendRequestEntity.setToMember(me);
            friendRequestEntity.setFromMember(friend);
            FriendRequestEntity savedFriendRequestEntity=friendRequestRepository.save(friendRequestEntity);

            return savedFriendRequestEntity.getId()>0;
        }  catch (DataAccessException e) {
            log.error("Database error addMemberInRequestFriend: {} ", e.getMessage());
            throw new CustomServiceException("Failed to addMemberInRequestFriend due to database error", e);
        } catch (Exception e) {
            log.error("addMemberInRequestFriend occurred {}", e.getMessage());
            throw new CustomServiceException("Failed to addMemberInRequestFriend", e);
        }
    }

    @Override
    public List<MemberDto> getRequestFriendToMe() {
        try {
            Long memberId=Long.parseLong(securityContextManager.getAuthenticatedUserName());
            List<FriendRequestEntity> entityList= friendRequestRepository.requestFriendList(memberId)
                    .orElseThrow(() -> new ResourceNotFoundException("not found List"));
            return entityList.stream()
                    .map(friendRequestEntity -> MemberDto.toDto(friendRequestEntity.getToMember()))
                    .toList();
        }catch (DataAccessException e) {
            log.error("Database error getRequestFriendListToMe: {} ", e.getMessage());
            throw new CustomServiceException("Failed to getRequestFriendListToMe due to database error", e);
        } catch (Exception e) {
            log.error("getRequestFriendListToMe occurred {}", e.getMessage());
            throw new CustomServiceException("Failed to getRequestFriendListToMe", e);
        }
    }

    @Transactional
    @Override
    public boolean deleteRequestAndJoinFriend(Long toMemberId) {
        boolean isSuccess= false;
        Long memberId=Long.parseLong(securityContextManager.getAuthenticatedUserName());

        //fromMemberId와 toMemberId로 친구 요청 entity를 삭제
        friendRequestRepository.deleteByFriendRequestIdAndMemberId(memberId,toMemberId);

        MemberEntity friend = new MemberEntity();
        friend.setId(toMemberId);

        MemberEntity me= new MemberEntity();
        me.setId(memberId);

        FriendEntity friendEntity= new FriendEntity();
        friendEntity.setFriend(friend);
        friendEntity.setMe(me);
        
        FriendEntity friendEntity2= new FriendEntity();
        friendEntity2.setFriend(me);
        friendEntity2.setMe(friend);

        // 본인과 친구 두명 다 friendEntity에 넣어주기
        FriendEntity savedFriendEntity= friendRepository.save(friendEntity);
        FriendEntity savedFriendEntity2= friendRepository.save(friendEntity2);

        if(savedFriendEntity.getId()>0 && savedFriendEntity2.getId()>0){
            isSuccess= true;
        }

        return isSuccess;
    }

    @Override
    public List<MemberDto> getFriends() {
        try{
            Long memberId=Long.parseLong(securityContextManager.getAuthenticatedUserName());
            List<FriendEntity> friends= friendRepository.findListWithFriendByMemberId(memberId)
                    .orElseThrow(() -> new ResourceNotFoundException("memberId: " + memberId + "not found friend"));
            return friends.stream()
                    .map(friendEntity -> MemberDto.toDto(friendEntity.getFriend()))
                    .collect(Collectors.toList());
        }catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리
            log.error("Database error getListFriend: {} ", e.getMessage());
            throw new CustomServiceException("Failed to get friend list due to database error", e);
        } catch (Exception e) {
            log.error("getFriendGroup occurred {}", e.getMessage());
            throw new CustomServiceException("Failed to get friend list", e);
        }


    }
}
