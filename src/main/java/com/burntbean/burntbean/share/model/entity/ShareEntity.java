package com.burntbean.burntbean.share.model.entity;

import com.burntbean.burntbean.member.model.entity.MemberEntity;
import com.burntbean.burntbean.room.model.entity.RoomEntity;
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
@Table(name = "share")
public class ShareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity roomEntity;

    @ManyToOne
    @JoinColumn(name = "share_id")
    private MemberEntity share; //화면공유 아이디

}
