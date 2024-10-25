package com.burntbean.burntbean.room.model.entity;

import com.burntbean.burntbean.common.entity.BaseEntity;
import com.burntbean.burntbean.common.enums.Rtype;
import com.burntbean.burntbean.room.model.dto.RoomDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room")
public class RoomEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "host_id")
    private Long hostId;
    @Column(name = "g_name")
    private String groupName;
    @Column(name= "total")
    private Integer total;
    @Enumerated(EnumType.STRING)
    private Rtype rtype;

    @OneToMany(mappedBy = "room" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomMemberEntity> roomMembers;

    public static RoomEntity toEntity(RoomDto dto){
        return RoomEntity.builder()
                .id(dto.getId())
                .hostId(dto.getHostId())
                .groupName(dto.getGroupName())
                .total(dto.getTotal())
                .rtype(dto.getRtype())
                .build();
    }


}
