package com.burntbean.burntbean.member.model.dto;

import com.burntbean.burntbean.member.model.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberDto {
    private Long id;
    private String email;
    private String name;
    private String nick;
    private LocalDate birth;
    private String createAt;
    private String modifiedAt;
    private String profile;
    private String status;

    public static MemberDto toDto(MemberEntity memberEntity){
        return MemberDto.builder()
                .id(memberEntity.getId())
                .email(memberEntity.getEmail())
                .name(memberEntity.getName())
                .nick(memberEntity.getNick())
                .birth(memberEntity.getBirth())
                .createAt(formattingFromCreateDate(memberEntity))
                .modifiedAt(formattingFromModifiedDate(memberEntity))
                .profile(memberEntity.getProfile())
                .status(String.valueOf(memberEntity.getStatus()))
                .build();
    }

    public static String formattingFromCreateDate(MemberEntity memberEntity) {
        if (memberEntity.getCreateAt() == null) {
            return ""; // 또는 기본값으로 설정할 문자열
        }
        LocalDateTime createAt = memberEntity.getCreateAt();
        return createAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static String formattingFromModifiedDate(MemberEntity memberEntity) {
        if (memberEntity.getModifiedAt() == null) {
            return ""; // 또는 기본값으로 설정할 문자열
        }
        LocalDateTime modifiedAt = memberEntity.getModifiedAt();
        return modifiedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

    }

}
