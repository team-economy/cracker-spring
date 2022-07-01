package com.cracker.user.service;

import com.cracker.user.dto.SignUpRequestDto;
import com.cracker.user.model.Users;
import com.cracker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "TODO";

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Users> findUsers() {
        return userRepository.findAll();
    }

    public Long deleteUser(Long id) {
        userRepository.deleteById(id);
        return id;
    }

    public Users registerUser(SignUpRequestDto signUpRequestDto) {
        //회원 검색
        String email = signUpRequestDto.getEmail();
        String name = signUpRequestDto.getName();
        String pic = signUpRequestDto.getPic();
        String marker_pic = signUpRequestDto.getMarker_pic();
        String pw = passwordEncoder.encode(signUpRequestDto.getPw());

        // 중복 검사
        Users dup = userRepository.findByEmail(email);
        if (!(dup == null)) {
            throw new IllegalArgumentException("중복된 사용자 ID가 존재합니다.");
        }

        // 사용자 Role
        String roles = "ROLE_USER";
        if (signUpRequestDto.getRole().equals("ROLE_ADMIN")) {
            if (!signUpRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            roles = "ROLE_ADMIN";
        }

        Users users = new Users(email, pw, name, pic, marker_pic, roles);
        userRepository.save(users);
        return users;
    }
}
