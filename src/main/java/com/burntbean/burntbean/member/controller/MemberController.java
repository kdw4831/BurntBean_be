package com.burntbean.burntbean.member.controller;

import com.burntbean.burntbean.member.model.dto.MemberDto;
import com.burntbean.burntbean.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;


    @PostMapping("/update")
    public ResponseEntity<String> addMember( @RequestBody MemberDto memberDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("birthday ="+ memberDto.getBirth());

        String newAccessJws =memberService.updateAndGiveNewAccessToken(memberDto, request,response);
        log.info("newAccessJws: {}", newAccessJws);
        if(newAccessJws !=null){
            log.info("newAccessJws: {}", newAccessJws);
            return ResponseEntity.ok().body(newAccessJws);
        };
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
    }


    /**
     *
     * @param nick
     * @return nick 존재하면 true 존재하지 않으면 false
     */
    @GetMapping("/nick")
    public ResponseEntity<Boolean> getMemberByNick(@RequestParam String nick) {

        Boolean isNickExist = memberService.findMemberNick(nick);
        log.info("isNickExist: {}", isNickExist);
        return  ResponseEntity.ok().body(isNickExist);
    }

    @GetMapping("/search/{nick}/{roomId}")
    public  ResponseEntity<List<String>> getNickList(@PathVariable String nick , @PathVariable Long roomId) {
        System.out.println("nick ="+ nick);
        List<String> result = memberService.searchNickListByRoomId(nick ,roomId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get_me")
    public ResponseEntity<MemberDto> getMember() {
        MemberDto memberDto= memberService.getMe();
        return ResponseEntity.ok().body(memberDto);
    }
}
