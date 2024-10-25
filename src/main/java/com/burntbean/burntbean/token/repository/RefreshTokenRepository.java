package com.burntbean.burntbean.token.repository;


import com.burntbean.burntbean.token.model.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {
    @Query("SELECT rt FROM RefreshTokenEntity rt LEFT JOIN FETCH rt.member m WHERE rt.token = :token")
    RefreshTokenEntity findByTokenWithMember(@Param("token") String token);
    int deleteByToken(String token);
    RefreshTokenEntity findByMemberId(Long memberId);
    int deleteByMemberId(Long memberId);

}
