package com.burntbean.burntbean.member.service;

import com.burntbean.burntbean.config.properties.TokenPropertiesConfig;
import com.burntbean.burntbean.exception.ResourceNotFoundException;
import com.burntbean.burntbean.member.model.dto.MemberDto;
import com.burntbean.burntbean.member.model.entity.MemberEntity;
import com.burntbean.burntbean.member.repository.MemberRepository;
import com.burntbean.burntbean.room.repository.RoomRepository;
import com.burntbean.burntbean.security.authentication.SecurityContextManager;
import com.burntbean.burntbean.token.model.BurntbeanJwt;
import com.burntbean.burntbean.token.repository.RefreshTokenRepository;
import com.burntbean.burntbean.token.service.JwtService;
import com.burntbean.burntbean.token.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final TokenPropertiesConfig tokenPropertiesConfig;

    private final RoomRepository roomRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityContextManager securityContextManager;
    //private final TransactionalOperator transactionalOperator;
    private final TokenService tokenService;


    @Override
    public String updateAndGiveNewAccessToken(MemberDto dto, HttpServletRequest request, HttpServletResponse response) {
        String accessToken=jwtService.getRefreshTokenFromCookies(request,"access-token");
        BurntbeanJwt jwt=BurntbeanJwt.fromJwt(accessToken, tokenPropertiesConfig.getAccessToken().getSecret());
        assert jwt != null;
        Long memberId =Long.parseLong(jwt.getSubject());
        Optional<MemberEntity> memberEntity = memberRepository.findById(memberId);
        if (memberEntity.isPresent()) {
            MemberEntity memberEntity1 = memberEntity.get();
            memberEntity1.setNick(dto.getNick());
            memberEntity1.setBirth(dto.getBirth());
            try {
                // Save the entity
                memberRepository.save(memberEntity1);
                // If save is successful, create and return the new access token
                String newAccessToken= jwtService.createAccessTokenFromMemberId(memberEntity1.getId(), true);
                tokenService.addCookieWithNewToken(response,newAccessToken);
                return newAccessToken;
            } catch (Exception e) {

                log.error("error while saving access token : {}", e.getMessage());

                return null;
            }
        }

        log.info("member not found");

        return null;
    }

    @Override
    public MemberDto getData(long memberId) {
        Optional<MemberEntity> optionalEntity=memberRepository.findById(memberId);
        MemberEntity entity = optionalEntity.get();
        return MemberDto.toDto(entity);
    }

    @Override
    public MemberDto getMe() {
        Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());
        Optional<MemberEntity> optionalEntity=memberRepository.findById(memberId);
        MemberEntity entity = optionalEntity.get();
        return MemberDto.toDto(entity);
    }

    @Override
    public Boolean findMemberNick(String nick) {
        Optional<MemberEntity> optionalEntity = memberRepository.findByNick(nick);
        if (optionalEntity.isPresent()) {
            MemberEntity entity = optionalEntity.get();
            log.info("NickName is: {}", entity.getNick());
            return true; // 닉네임이 존재함
        }
        return false; // 닉네임이 존재하지 않음
    }

    @Override
    public List<String> searchNickListByRoomId(String nick, Long roomId) {

        return  memberRepository.findMemberListExcludingGroupMemberAndAlreadyRequestedList(nick, roomId).orElseThrow(()-> new ResourceNotFoundException("no resource about "+nick))
                .stream()
                .map(MemberEntity::getNick)
                .toList();
    }

    @Override
    public Boolean deleteMember() {
        return false;
        // 삭제로직은 천천히 찾아보기
        // 메세지기록은 남기는 느낌으로 갑시당
    }

    @Override
    public List<MemberDto> getMembers() {
        List<MemberDto> members = memberRepository.findAll()
                .stream()
                .map((memberEntity) ->MemberDto.toDto(memberEntity))
                .toList();
        return members;
    }
}
