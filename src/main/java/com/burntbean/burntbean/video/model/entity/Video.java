package com.burntbean.burntbean.video.model.entity;

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
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "room_id", foreignKey = @ForeignKey(name = "room_video_fk"))
    private RoomEntity roomEntity;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private MemberEntity host;
    private boolean status;
}
