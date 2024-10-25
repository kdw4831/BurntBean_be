package com.burntbean.burntbean.member.controller;


import com.burntbean.burntbean.member.service.UserService;
import com.burntbean.burntbean.token.model.dto.TokenRequest;
import com.burntbean.burntbean.token.service.TokenService;
import com.burntbean.burntbean.token.util.GoogleTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;
    private final TokenService tokenService;
    private final GoogleTokenVerifier googleTokenVerifier;

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("go to WebView");
    }

    @GetMapping("/login/success")
    public  ResponseEntity<String> loginSuccess(){
        return ResponseEntity.status(HttpStatus.CREATED).body("login success");

    }

    @GetMapping("/login/fail")
    public  ResponseEntity<String>  loginFail(){

        return ResponseEntity.status(HttpStatus. UNAUTHORIZED).body("login fail");

    }

    /**
     * logout
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        boolean isSuccess= tokenService.deleteRefreshTokenWithContextHolderFromMemberId();
        if(isSuccess){
            return ResponseEntity.status(HttpStatus.OK).body("logout success");
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("logout fail");
        }

    }


    @PostMapping("/login/google")
    public ResponseEntity<?> googleLogin(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {
        try {
            log.info("google-token jws: " , tokenRequest.getIdToken());
            GoogleIdToken.Payload payload = googleTokenVerifier.verify(tokenRequest.getIdToken());
            System.out.println("google-token jws: " + tokenRequest.getIdToken());

            //사용자 정보 추출 (이메일만 필요)
            String email = payload.getEmail();
            String profile=(String)payload.get("picture");
            String name = (String)payload.get("name");

            log.info("google-token email: " + email);
            log.info("google-token picture-url: " + profile);

            userService.checkAndJoinUser(email,profile,name,response);
            log.info("connected success");

            return ResponseEntity.ok().body("JWT Token generated successfully");
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(401).body("Invalid ID token.");
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }


}

