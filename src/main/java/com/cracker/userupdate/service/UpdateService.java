package com.cracker.userupdate.service;

import com.cracker.user.entity.Users;
import com.cracker.user.repository.UserRepository;
import com.cracker.userupdate.dto.UpdateUserRequestDto;
import com.cracker.userupdate.dto.UpdateUserResponseDto;
import com.cracker.userupdate.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class UpdateService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional
    public UpdateUserResponseDto updateProfile(Long id, UpdateUserRequestDto updateUserRequestDto, MultipartFile file)
            throws IOException {
        Users users = userRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 사용자 없음")
        );

        String filepath = null;

        if (file != null) {
            filepath = s3Service.upload(file);
        }

        users.updateUserProfile(updateUserRequestDto, filepath);

        userRepository.save(users);

        UpdateUserResponseDto updateUserResponseDto = new UpdateUserResponseDto();
        updateUserResponseDto.setMsg("변경 완료!!");

        return updateUserResponseDto;
    }
}