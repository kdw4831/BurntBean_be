package com.burntbean.burntbean.room.model.dto;

import com.burntbean.burntbean.common.enums.Rtype;
import com.burntbean.burntbean.member.model.dto.MemberDto;
import com.burntbean.burntbean.room.model.entity.RoomEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;
    private Long hostId;
    private String groupName;
    private Integer total;
    private Rtype rtype;
    List<MemberDto> members;

    public static RoomDto toDto(RoomEntity entity){
        return RoomDto.builder()
                .id(entity.getId())
                .hostId(entity.getHostId())
                .groupName(entity.getGroupName())
                .total(entity.getTotal())
                .rtype(entity.getRtype())
                .build();
    }

}
