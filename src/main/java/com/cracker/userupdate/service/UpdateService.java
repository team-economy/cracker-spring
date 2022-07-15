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
    public UpdateUserResponseDto updateProfile(Long id, UpdateUserRequestDto updateUserRequestDto)
            throws IOException {
        Users users = userRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 사용자 없음")
        );

            String nickname = updateUserRequestDto.getNickname();
            String statusMessage = updateUserRequestDto.getStatusMessage();
            MultipartFile file = updateUserRequestDto.getFile();


        String userMail = users.getEmail();
        String filepath = null;

        try {
            String contentType = file.getContentType();
            String originalFileExtensionBack = null;
            if(contentType.contains("image/jpeg")){
                originalFileExtensionBack = ".jpg";
            }
            else if(contentType.contains("image/png")){
                originalFileExtensionBack = ".png";
            }
            else if(contentType.contains("video/mp4")){
                originalFileExtensionBack = ".mp4";
            }

            if (file != null) {
                filepath = s3Service.upload(file, userMail + originalFileExtensionBack);
            }
        } catch (NullPointerException e) {
            filepath = users.getPic();
        }



        users.updateUserProfile(updateUserRequestDto, filepath);

        userRepository.save(users);

        UpdateUserResponseDto updateUserResponseDto = new UpdateUserResponseDto();
        updateUserResponseDto.setMsg("변경 완료!!");

        return updateUserResponseDto;
    }
}