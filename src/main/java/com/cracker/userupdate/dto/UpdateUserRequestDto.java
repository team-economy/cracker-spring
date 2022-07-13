package com.cracker.userupdate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequestDto {
    private String nickname;
    private MultipartFile file;
    private String statusMessage;
}
