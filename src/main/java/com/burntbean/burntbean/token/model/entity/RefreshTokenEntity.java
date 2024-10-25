package com.burntbean.burntbean.token.model.entity;

import com.burntbean.burntbean.member.model.entity.MemberEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "refresh_token",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "refresh_member_id_unique",
                        columnNames = {"member_id"}
                )
        }

    )
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id")
    private Long memberId;
    private String token;
    @Column(name= "is_nick_exist")
    private boolean isNickExist;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expire_date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private MemberEntity member;

    public boolean isExpired() {
        if (expire_date == null) {
            return false;
        }
        return expire_date.isBefore(LocalDateTime.now());
    }
}
