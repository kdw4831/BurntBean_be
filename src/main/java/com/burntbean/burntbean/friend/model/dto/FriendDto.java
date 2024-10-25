package com.burntbean.burntbean.friend.model.dto;


import com.burntbean.burntbean.friend.model.entity.FriendEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FriendDto {
    private Long id;
    private Long roomId;
    private String email;
    private String name;
    private String profile;
    private String status;



    // Entity to DTO 변환 메서드
    public static FriendDto toDto(FriendEntity entity) {
        return FriendDto.builder()
                .id(entity.getId())
                .email(entity.getFriend().getEmail()) // 친구의 이메일
                .name(entity.getFriend().getNick()) // 친구의 이름
                .profile(entity.getFriend().getProfile()) // 친구의 프로필 (가정)
                .status(String.valueOf(entity.getFriend().getStatus())) // 친구의 상태 (가정)
                .build();
    }


}
