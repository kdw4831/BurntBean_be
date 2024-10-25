package com.burntbean.burntbean.friend.model.entity;

import com.burntbean.burntbean.member.model.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="friend_request")
public class FriendRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id", foreignKey = @ForeignKey(name="member_from_id_request_fk"))
    private MemberEntity fromMember;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id" , foreignKey = @ForeignKey(name="member_from_id_request_fk"))
    private MemberEntity toMember;
}
