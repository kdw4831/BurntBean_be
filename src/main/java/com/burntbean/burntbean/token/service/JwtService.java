package com.burntbean.burntbean.token.service;



import com.burntbean.burntbean.config.properties.TokenPropertiesConfig;
import com.burntbean.burntbean.member.model.entity.MemberEntity;
import com.burntbean.burntbean.member.repository.MemberRepository;
import com.burntbean.burntbean.token.model.entity.RefreshTokenEntity;
import com.burntbean.burntbean.token.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService { //jwt를 사용해서 jwt 생성하고 유효한 토큰인지 검증하는 클래스

    private final TokenPropertiesConfig tokenPropertiesConfig;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;


    private String accessSecret;
    private long accessExpiration;



    @PostConstruct
    public void init(){
        accessSecret = tokenPropertiesConfig.getAccessToken().getSecret();
        accessExpiration = tokenPropertiesConfig.getAccessToken().getExpiration();
    }




    public String createAccessTokenFromMemberId(Long memberId,boolean isExistNick){
        SecretKey secretKey=Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        long currentMs=System.currentTimeMillis();
        return Jwts.builder()
                .subject(memberId.toString())
                .claim("isNickExist",isExistNick)
                .expiration(new Date(currentMs+1000*accessExpiration))
                .signWith(secretKey)
                .issuedAt(new Date(currentMs))
                .compact();
    }

    /**
     * email 로 refreshToken 삭제  logout 시에 삭제
     * @param refreshJws
     * @return
     */
    public boolean deleteRefreshTokenByemail(String refreshJws){
        boolean isSuccess=false;

        int result = refreshTokenRepository.deleteByToken(refreshJws);

        if(result>0){
            isSuccess=true;
        }

        return isSuccess;

    }


    /**
     * refreshToken으로 AccessToken 생성
     * @param request
     * @return
     */
    @Transactional
    public String createAccessTokenFromRefreshToken(HttpServletRequest request){
        String refreshJws= getRefreshTokenFromCookies(request,"refresh-token");
        RefreshTokenEntity refreshTokenEntity= refreshTokenRepository.findByTokenWithMember(refreshJws);
        if(refreshTokenEntity==null || refreshTokenEntity.isExpired()){

            if (refreshTokenEntity != null) {
                log.debug("refresh token expired");
                refreshTokenRepository.deleteByToken(refreshJws);
                log.info("refresh token delete");
            }
            log.debug("refresh token is null");
            return null;
        }

        boolean isNickExist = refreshTokenEntity.getMember().getNick() != null;
        return createAccessTokenFromMemberId(refreshTokenEntity.getMemberId(),isNickExist);
    }

    // 쿠키를 가져오는 메서드
    public String getRefreshTokenFromCookies(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenName.equals(cookie.getName())) {
                    return cookie.getValue(); // 쿠키 값 반환
                }
            }
        }
        return null; // 해당 쿠키가 없는 경우
    }

    /**
     * member id로 refresh token 생성
     * @param memberId
     * @return
     */

    @Transactional
    public String createRefreshTokenFromMemberId(Long memberId){
        Optional<MemberEntity> optionalEntity=memberRepository.findById(memberId);
        MemberEntity memberentity = optionalEntity.get();

        RefreshTokenEntity entity= new RefreshTokenEntity();
        entity.setMemberId(memberId);
        entity.setToken(UUID.randomUUID().toString());
        entity.setExpire_date(LocalDateTime.now().plusSeconds(tokenPropertiesConfig.getRefreshToken().getExpiration()));


        RefreshTokenEntity existingToken = refreshTokenRepository.findByMemberId(memberentity.getId());
        if(existingToken!=null){

            //refreshTokenRepository.save(entity); //  update
            existingToken.setToken(entity.getToken());
            existingToken.setExpire_date(entity.getExpire_date());


        }else{
            refreshTokenRepository.save(entity); // 존재하지 않으면 insert

        }

        return entity.getToken();
    }
 }
