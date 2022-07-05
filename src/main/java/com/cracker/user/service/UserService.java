package com.cracker.user.service;

import com.cracker.user.dto.UserRequestDto;
import com.cracker.user.model.UserRole;
import com.cracker.user.model.Users;
import com.cracker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "TODO";

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserRequestDto requestDto) {

        String email = requestDto.getEmail();
        // 회원 ID 중복 확인
        Optional<Users> Ifound = userRepository.findByEmail(email);
        if (Ifound.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

        String nickname = requestDto.getNickname();
        // 회원 닉네임 중복 확인
        Optional<Users> Nfound = userRepository.findByNickname(nickname);
        if (Nfound.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 닉네임이 존재합니다.");
        }

        // 패스워드 인코딩
        String pw = passwordEncoder.encode(requestDto.getPw());
        String pic = "123";
        String marker_pic = "123";
//        String pic = "static/profile_pics/profile_placeholder.png";
//        String marker_pic = "static/profile_pics/profile_placeholder.png";

        // 사용자 ROLE 확인
        String role = "ROLE_USER";
//        UserRole role = requestDto.getRole();
//        if (requestDto.getRole().equals(UserRole.ROLE_ADMIN)) {
//            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
//                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
//            }
//            role = UserRole.ROLE_ADMIN;
//        }
        Users users = new Users(email, pw, nickname, pic, marker_pic, role);
        userRepository.save(users);
    }

    public String checkEmailDup(String email) {
        // 회원 ID 중복 확인
        Optional<Users> found = userRepository.findByEmail(email);
        if (found.isPresent()) {
            return "exists";
        } else {
            return "Email Duplication Checked.";
        }
    }

    public String checkNicknameDup(String nickname) {
        // 회원 닉네임 중복 확인
        Optional<Users> found = userRepository.findByNickname(nickname);
        if (found.isPresent()) {
            return "exists";
        } else {
            return "Nickname Duplication Checked.";
        }
    }

    public List<Users> findUsers() {
        return userRepository.findAll();
    }
}
