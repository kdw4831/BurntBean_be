package com.burntbean.burntbean.member.service;

import com.burntbean.burntbean.member.model.dto.MemberDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface MemberService {
    public String updateAndGiveNewAccessToken(MemberDto dto, HttpServletRequest request, HttpServletResponse response);
    public MemberDto getData(long memberId);
    public MemberDto getMe();
    public Boolean findMemberNick(String nick);
    public List<String> searchNickListByRoomId(String nick , Long roomId);
    public Boolean deleteMember();
    public List<MemberDto> getMembers();

}
