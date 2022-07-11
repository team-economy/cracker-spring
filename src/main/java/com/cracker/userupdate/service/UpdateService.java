package com.cracker.userupdate.service;

import com.cracker.user.entity.Users;
import com.cracker.user.repository.UserRepository;
import com.cracker.userupdate.S3Manager.S3Manager;
import com.cracker.userupdate.dto.UpdateUserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class UpdateService {

    private final UserRepository userRepository;
    private final S3Manager s3Manager;

    public Users updateProfile(Long id, UpdateUserRequestDto updateUserRequestDto, MultipartFile multipartFile)//UserdetailsImpl nowUser
            throws IOException {
        Users users = userRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 사용자 없음")
        );
        //닉네임,사진,상태메세지 변경 했을때

        if (nickname != null && multipartFile != null && statusMessage != null) {
            users.updateUserProfile(updateUserRequestDto);
            String pic = s3Manager.upload(multipartFile, "userpic"); //s3에 userpic 폴더에 저장하고 cloudfront url 반환
            users.setPic(pic);
        } else if (nickname != null && multipartFile == null && ) {
        }

        userRepository.save(users);
        return users;
    }
}

    //    @Transactional
//    public void bookshelfUpdateInfo(User user, Long id, MultipartFile file, BookshelfInfoUpdateReqDto req) {
//        Book book = commonService.getBook(user, id);
//        String thumbnail = book.getThumbnail();
//        if (file != null) {
//            try {
//                thumbnail = s3Service.upload(file);
//            } catch (IOException e) {
//                throw new S3Exception("file = " + file.getOriginalFilename());
//            }
//        }
//        book.updateBookInfo(req.getTitle(), req.getAuthor(), req.getPublisher(), req.getTotPage(), thumbnail);
//        bookRepository.save(book);


//    public long update(Long id, UpdateUserRequestDto updateUserRequestDto){
//        Users users = UsersRepository.findById(id).orElseThrow(
//                ()-> new IllegalArgumentException("아이디를 찾을수 없습니다")
//        );
//
//        users.update(updateUserRequestDto.getNickname());
//        users.update(updateUserRequestDto.getStatus());
//
//        MultipartFile multipartFile = updateUserRequestDto.getPic();
//
//        if
//    }

//}
