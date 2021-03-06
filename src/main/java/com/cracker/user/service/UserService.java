package com.cracker.user.service;

import com.cracker.auth.dto.KakaoUserInfo;
import com.cracker.auth.security.UserPrincipal;
import com.cracker.auth.security.social.KakaoOAuth2;
import com.cracker.auth.util.token.AuthToken;
import com.cracker.auth.dto.TokenDto;
//import com.cracker.cracker.auth.security.social.KakaoOAuth2;
import com.cracker.auth.properties.AppProperties;
import com.cracker.auth.service.AuthService;
import com.cracker.user.dto.JoinDto;
//import com.cracker.cracker.user.dto.KakaoUserInfo;
import com.cracker.user.entity.UserRole;
import com.cracker.user.entity.Users;
import com.cracker.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;
    private final KakaoOAuth2 kakaoOAuth2;

    @Value("${kakao.adminToken}")
    private String ADMIN_TOKEN;

    /**
     * 이메일 중복 체크
     */
    @Transactional
    public boolean emailDuplicate(String email) {
        Optional<Users> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    /**
     * 닉네임 중복 체크
     */
    @Transactional
    public boolean nicknameDuplicate(String nickname) {
        Optional<Users> user = userRepository.findByNickname(nickname);
        return user.isPresent();
    }

    /**
     * 회원가입
     */
    @Transactional
    public TokenDto join(HttpServletResponse response, JoinDto requestJoinDTO) {
        // adminToken 설정시 사용
        String email = requestJoinDTO.getEmail();

        // 이메일 중복 확인
        if (emailDuplicate(email)) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

        String nickname = requestJoinDTO.getNickname();
        // 회원 닉네임 중복 확인
        if (nicknameDuplicate(nickname)) {
            throw new IllegalArgumentException("중복된 사용자 닉네임이 존재합니다.");
        }

        String pic;
        if (requestJoinDTO.getPic().equals("")) {
            pic = "/profile_pics/profile_placeholder.png";
        } else {
            pic = requestJoinDTO.getPic();
        }

        String marker_pic = "/marker_pics/marker-default.png";

        // 사용자 ROLE 확인
        UserRole role = UserRole.USER;
        if (!requestJoinDTO.getAdminToken().equals("")) {
            if (!requestJoinDTO.getAdminToken().equals(appProperties.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRole.ADMIN;
        }

        Users user = new Users(email, nickname, pic, marker_pic, role);

        AuthToken refreshToken = authService.refreshToken(user);
        user.updateRefreshToken(refreshToken.getToken());
        AuthToken accessToken = authService.AccessToken(user);

        user.updateUserPassword(passwordEncoder.encode(requestJoinDTO.getPassword()));
        authService.refreshTokenAddCookie(response, refreshToken.getToken());

        userRepository.save(user);

        return new TokenDto(accessToken.getToken());
    }

    /**
     * 이메일 중복 체크
     */
    @Transactional
    public Boolean duplicateEmailCheck(Map<String, String> requestObject) {
        String email = requestObject.get("email");
        return emailDuplicate(email);
    }

    /**
     * 이메일 유효성 체크
     */
    public Boolean validateEmailCheck(Map<String, String> requestObject) {
        String regExp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String email = requestObject.get("email");

        return email.matches(regExp);
    }

    /**
     * 닉네임 중복 체크
     */
    @Transactional
    public Boolean duplicateNicknameCheck(Map<String, String> requestObject) {
        String nickname = requestObject.get("nickname");
        return nicknameDuplicate(nickname);
    }

    /**
     * 닉네임 유효성 체크
     */
    public Boolean validateNicknameCheck(Map<String, String> requestObject) {
        String regExp = "^([a-zA-Z0-9ㄱ-ㅎ가-힣]).{1,10}$";
        String nickname = requestObject.get("nickname");
        return nickname.matches(regExp);
    }

    /**
     * 비밀번호 유효성 체크
     */
    public Boolean validatePasswordCheck(String password){
        String regExp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%^&*_?]{8,20}$";
        return password.matches(regExp);
    }

    /**
     * 카카오 로그인 시 DB에 회원 정보가 있다면 토큰 발급
     */
    @Transactional
    public TokenDto getToken(Users user, HttpServletResponse response) {
        // refresh token 발급 및 쿠키에 저장
        AuthToken refreshToken = authService.refreshToken(user);
        user.updateRefreshToken(refreshToken.getToken());
        authService.refreshTokenAddCookie(response, refreshToken.getToken());

        // access token 발급
        AuthToken accessToken = authService.AccessToken(user);

        return new TokenDto(accessToken.getToken());
    }

    /**
     * 카카오 로그인 시 DB에 회원 정보가 있다면 비밀번호 체크 로직 실행
     */
    @Transactional
    public TokenDto kakaoLogin(KakaoUserInfo userInfo, Users user, HttpServletResponse response) {
        Long kakaoId = userInfo.getId();
        // 패스워드를 카카오 Id + ADMIN TOKEN 로 지정
        String password = kakaoId + ADMIN_TOKEN;
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        return getToken(user, response);
    }

    /**
     * 카카오 로그인 시 DB에 회원 정보가 없다면 회원가입 로직 실행
     */
    @Transactional
    public TokenDto kakaoJoin(KakaoUserInfo userInfo, HttpServletResponse response) {
        Long kakaoId = userInfo.getId();
        // 패스워드를 카카오 Id + ADMIN TOKEN 로 지정
        String password = kakaoId + ADMIN_TOKEN;
        String marker_pic = "static/profile_pics/profile_placeholder.png";
        String role = UserRole.USER.getCode();
        String adminToken = "";
        JoinDto joinDto = new JoinDto(userInfo.getEmail(), password, userInfo.getEmail(), userInfo.getPic(), marker_pic, role, adminToken);
        return join(response, joinDto);
    }

    /**
     * 프론트에서 카카오 로그인 클릭 시 실행되는 서비스 로직
     */
    @Transactional
    public TokenDto kakao(String authorizedCode, HttpServletResponse response) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);

        // 이메일 중복 체크 진행 → 중복이라면 이미 가입된 유저임
        Optional<Users> userOptional = userRepository.findByEmail(userInfo.getEmail());
        TokenDto token;
        Users user;
        if (userOptional.isPresent()) {
            // 이미 존재하는 회원이면 토큰 발급
            token = kakaoLogin(userInfo, userOptional.get(), response);
            user = userOptional.get();
        } else {
            // 없는 회원이라면 회원가입 진행
            token = kakaoJoin(userInfo, response);
            // 기존 userOptional은 null이므로 회원가입 후 null이 아닌 userOptionalJoin으로 해당 User 확인
            Optional<Users> userOptionalJoin = userRepository.findByEmail(userInfo.getEmail());
            user = userOptionalJoin.get();
        }
        AuthToken accessToken = authService.AccessToken(user);
        authService.accessTokenAddCookie(response, accessToken.getToken());

        return token;
    }

    @Transactional
    public Users userSearch(String nickname) {
        return userRepository.getByNickname(nickname);
    }
}
