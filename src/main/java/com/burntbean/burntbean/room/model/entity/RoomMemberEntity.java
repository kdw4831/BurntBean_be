package com.burntbean.burntbean.room.model.entity;

import com.burntbean.burntbean.common.entity.BaseEntity;
import com.burntbean.burntbean.member.model.entity.MemberEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_member")
public class RoomMemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id" ,foreignKey = @ForeignKey(name="room_member_fk"))
    private RoomEntity room;
    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "member_room_fk"))
    private MemberEntity member;


    public static RoomMemberEntity ToRoomMemberEntity(RoomEntity roomEntity, Long memberId){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberId);
        RoomMemberEntity roomMemberEntity = new RoomMemberEntity();
        roomMemberEntity.setRoom(roomEntity);
        roomMemberEntity.setMember(memberEntity);
        return roomMemberEntity;
    }
}
