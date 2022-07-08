package com.cracker.cracker.user.service;

import com.cracker.cracker.auth.dto.TokenDto;
//import com.cracker.cracker.auth.security.KakaoOAuth2;
import com.cracker.cracker.auth.properties.AppProperties;
import com.cracker.cracker.auth.service.AuthService;
import com.cracker.cracker.auth.util.token.AuthToken;
import com.cracker.cracker.user.dto.JoinDto;
//import com.cracker.cracker.user.dto.KakaoUserInfo;
import com.cracker.cracker.user.entity.UserRole;
import com.cracker.cracker.user.entity.Users;
import com.cracker.cracker.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.usertype.UserType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;
//    private final RabbitMqSender rabbitMqSender;
//    private final KakaoOAuth2 kakaoOAuth2;

//    @Value("${kakao.adminToken}")
//    private String ADMIN_TOKEN;


    /**
     * 이메일 중복 체크
     */
    public boolean emailDuplicate(String email) {
        Optional<Users> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    /**
     * 닉네임 중복 체크
     */
    public boolean nicknameDuplicate(String nickname) {
        Optional<Users> user = userRepository.findByNickname(nickname);
        return user.isPresent();
    }

    /**
     * 회원가입
     */
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

        String pic = "static/profile_pics/profile_placeholder.png";
        String marker_pic = "static/profile_pics/profile_placeholder.png";

        // 사용자 ROLE 확인
        UserRole role = UserRole.USER;
        if (!requestJoinDTO.getAdminToken().equals("")) {
            if (!requestJoinDTO.getAdminToken().equals(appProperties.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRole.ADMIN;
        }
        Users user = new Users(email, nickname, pic, marker_pic, role);

//        // admin 신경 안쓸 때 사용
//        Users user = new Users(requestJoinDTO, uuid);
//        // 이메일이 중복되는 경우
//        if (emailDuplicate(requestJoinDTO.getEmail())) {
//            return null;
//        }

        AuthToken refreshToken = authService.refreshToken(user);
        user.updateRefreshToken(refreshToken.getToken());
        AuthToken accessToken = authService.AccessToken(user);

        user.updateUserPassword(passwordEncoder.encode(requestJoinDTO.getPassword()));
        authService.refreshTokenAddCookie(response, refreshToken.getToken());

        userRepository.save(user);

//        UserDto userDto = new UserDto(requestJoinDTO.getEmail(), requestJoinDTO.getNickName(), uuid);
//
//        rabbitMqSender.send(userDto);

        return new TokenDto(accessToken.getToken());
    }

    /**
     * 이메일 중복 체크
     */
    public Boolean duplicateEmailCheck(Map<String, String> requestObject) {
        String email = requestObject.get("email");
        return emailDuplicate(email);
    }

    /**
     * 이메일 중복 체크
     */
    public Boolean duplicateNicknameCheck(Map<String, String> requestObject) {
        String nickname = requestObject.get("nickname");
        return nicknameDuplicate(nickname);
    }

//    /**
//     * 카카오 로그인 시 DB에 회원 정보가 있다면 토큰 발급
//     */
//    public TokenDto getToken(Users user, HttpServletResponse response) {
//        // refresh token 발급 및 쿠키에 저장
//        AuthToken refreshToken = authService.refreshToken(user);
//        user.updateRefreshToken(refreshToken.getToken());
//        authService.refreshTokenAddCookie(response, refreshToken.getToken());
//
//        // access token 발급
//        AuthToken accessToken = authService.AccessToken(user);
//
//        return new TokenDto(accessToken.getToken());
//    }

//    /**
//     * 카카오 로그인 시 DB에 회원 정보가 있다면 비밀번호 체크 로직 실행
//     */
//    public TokenDto kakaoLogin(KakaoUserInfo userInfo, UserCertification user, HttpServletResponse response) {
//        Long kakaoId = userInfo.getId();
//        // 패스워드를 카카오 Id + ADMIN TOKEN 로 지정
//        String password = kakaoId + ADMIN_TOKEN;
//        if (!passwordEncoder.matches(password, user.getLoginPassword())) {
//            return null;
//        }
//        return getToken(user, response);
//    }
//
//    /**
//     * 카카오 로그인 시 DB에 회원 정보가 없다면 회원가입 로직 실행
//     */
//    public TokenDto kakaoJoin(KakaoUserInfo userInfo, HttpServletResponse response) {
//        Long kakaoId = userInfo.getId();
//        // 패스워드를 카카오 Id + ADMIN TOKEN 로 지정
//        String password = kakaoId + ADMIN_TOKEN;
//        JoinDto joinDto = new JoinDto(userInfo.getEmail(), password, userInfo.getNickname(), "GENERAL");
//        return join(response, joinDto, Type.valueOf("SOCIAL"));
//    }
//
//    /**
//     * 프론트에서 카카오 로그인 클릭 시 실행되는 서비스 로직
//     */
//    public TokenDto kakao(String authorizedCode, HttpServletResponse response) {
//        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
//        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
//
//        // 이메일 중복 체크 진행 → 중복이라면 이미 가입된 유저임
//        Optional<UserCertification> user = userCertificationRepository.findByLoginId(userInfo.getEmail());
//        TokenDto token;
//        if (user.isPresent()) {
//            // 이미 존재하는 회원이면 토큰 발급
//            token = kakaoLogin(userInfo, user.get(), response);
//        } else {
//            token = kakaoJoin(userInfo, response);
//        }
//        return token;
//    }
}
