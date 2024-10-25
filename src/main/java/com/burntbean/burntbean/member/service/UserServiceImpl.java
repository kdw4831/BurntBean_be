package com.burntbean.burntbean.member.service;


import com.burntbean.burntbean.common.enums.Status;

import com.burntbean.burntbean.friend.controller.StatusController;
import com.burntbean.burntbean.member.model.entity.MemberEntity;
import com.burntbean.burntbean.member.repository.MemberRepository;
import com.burntbean.burntbean.token.service.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final StatusController statusController;

    @Override
    public void checkAndJoinUser(String email,String profile,String name, HttpServletResponse response) {
        MemberEntity memberEntity = memberRepository.findByEmail(email);
        if (memberEntity == null) {
            MemberEntity member = new MemberEntity();
            member.setEmail(email);
            member.setProfile(profile);
            member.setName(name);
            MemberEntity savedMember = memberRepository.save(member);
            Long memberId = savedMember.getId();
            tokenService.createTokenAndAddCookie(memberId, response,false);
        } else {
            Long memberId = memberEntity.getId();
            log.info("nick is: {}",  memberEntity.getNick());
            boolean isNickExist = memberEntity.getNick() != null;
            tokenService.createTokenAndAddCookie(memberId, response,isNickExist);

        }

    }

    @Override
    @Transactional
    public void restoreUserStatus(Long userId) {
        MemberEntity member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        member.setStatus(Status.online);
        memberRepository.save(member);

        statusController.updateUserStatus(userId, "online");
    }
    @Override
    @Transactional
    public void userLoggedOut(Long userId) {
        MemberEntity member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        member.setStatus(Status.offline);
        memberRepository.save(member);

        statusController.updateUserStatus(userId, "offline");
    }
}