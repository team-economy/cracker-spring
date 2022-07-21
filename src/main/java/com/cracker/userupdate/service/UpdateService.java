package com.cracker.userupdate.service;

import com.cracker.user.entity.Users;
import com.cracker.user.repository.UserRepository;
import com.cracker.userupdate.dto.UpdateMarkerRequestDto;
import com.cracker.userupdate.dto.UpdateMarkerResponseDto;
import com.cracker.userupdate.dto.UpdateUserRequestDto;
import com.cracker.userupdate.dto.UpdateUserResponseDto;
import com.cracker.userupdate.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@Service
public class UpdateService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    //파일명 암호화를 위한 세팅
    public String encrypt(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());

        return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    @Transactional
    public UpdateUserResponseDto updateProfile(Long id, UpdateUserRequestDto updateUserRequestDto)
            throws IOException, NoSuchAlgorithmException {
        Users users = userRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 사용자 없음")
        );

            String nickname = updateUserRequestDto.getNickname();
            String statusMessage = updateUserRequestDto.getStatusMessage();
            MultipartFile file = updateUserRequestDto.getFile();


        String mail = users.getEmail();
        String userMail = encrypt(mail);
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


    @Transactional
    public UpdateMarkerResponseDto updateMarker(Long id, UpdateMarkerRequestDto updateMarkerRequestDto)
            throws IOException, NoSuchAlgorithmException {
        Users users = userRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 사용자 없음")
        );

        String mail = users.getEmail();
        String userMail = encrypt(mail);
        MultipartFile file = updateMarkerRequestDto.getFile();

        String markerpath = null;

        try {
            String contentType = file.getContentType();
            String originalFileExtensionBack = null;
            if (contentType.contains("image/jpeg")) {
                originalFileExtensionBack = ".jpg";
            } else if (contentType.contains("image/png")) {
                originalFileExtensionBack = ".png";
            } else if (contentType.contains("video/mp4")) {
                originalFileExtensionBack = ".mp4";
            }

            if (file != null) {
                markerpath = s3Service.upload(file, userMail + "marker" + originalFileExtensionBack);
            }
        } catch (NullPointerException e) {
            markerpath = users.getMarker_pic();
        }

        users.updateUserMarker(markerpath);

        userRepository.save(users);

        UpdateMarkerResponseDto updateMarkerResponseDto = new UpdateMarkerResponseDto();
        updateMarkerResponseDto.setMsg("변경 완료!!");

        return updateMarkerResponseDto;

    }

}