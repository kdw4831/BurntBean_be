package com.burntbean.burntbean.friend.model.entity;


import com.burntbean.burntbean.friend.model.dto.FriendDto;
import com.burntbean.burntbean.member.model.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "friend")
public class FriendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "my_id")
    private MemberEntity me;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private MemberEntity friend;


}
