package com.burntbean.burntbean.message.model.entity;

import com.burntbean.burntbean.common.entity.BaseEntity;
import com.burntbean.burntbean.member.model.entity.MemberEntity;
import com.burntbean.burntbean.message.model.dto.MessageDto;
import com.burntbean.burntbean.room.model.entity.RoomEntity;
import jakarta.persistence.*;
import lombok.*;


@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "message")
public class MessageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id"
            ,referencedColumnName = "member_id"
            , foreignKey = @ForeignKey(name="message_member_fk"))
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id", foreignKey = @ForeignKey(name="message_chat_fk"))
    private RoomEntity room;
    private String content;


    public static MessageEntity toEntity(MessageDto dto , Long memberId , Long roomId ) {
        MemberEntity memberEntity=new MemberEntity();
        RoomEntity roomEntity= new RoomEntity();
        memberEntity.setId(memberId);
        roomEntity.setId(roomId);
        return  MessageEntity.builder()
                .room(roomEntity)
                .member(memberEntity)
                .content(dto.getContent())
                .build();
    }
}
