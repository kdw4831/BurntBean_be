package com.burntbean.burntbean.member.model.entity;


import com.burntbean.burntbean.common.entity.BaseEntity;
import com.burntbean.burntbean.common.enums.Status;
import com.burntbean.burntbean.friend.model.entity.FriendEntity;
import com.burntbean.burntbean.member.model.dto.MemberDto;
import com.burntbean.burntbean.room.model.entity.RoomMemberEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "member")
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(unique = true)
    private String email;
    @Column(name = "name")
    private String name;
    @Column(unique = true)
    private String nick;
    private LocalDate birth;
    private String profile;
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "member" ,cascade = CascadeType.ALL, orphanRemoval = true )
    private List<RoomMemberEntity> roomList;

    @OneToMany(mappedBy = "me")
    private List<FriendEntity> friends;

    public static MemberEntity toEntity(MemberDto dto) {
        return MemberEntity.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .nick(dto.getNick())
                .birth(dto.getBirth())
                .status(Status.fromString(dto.getStatus()))
                .profile(dto.getProfile())
                .build();
    }

}
