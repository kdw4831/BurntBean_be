package com.burntbean.burntbean.room.model.entity;


import com.burntbean.burntbean.member.model.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_request")
public class RoomRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", foreignKey = @ForeignKey(name="room_id_request_fk"))
    private RoomEntity room;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "invite_id" , foreignKey = @ForeignKey(name="member_id_request_fk"))
    private MemberEntity member;
}
