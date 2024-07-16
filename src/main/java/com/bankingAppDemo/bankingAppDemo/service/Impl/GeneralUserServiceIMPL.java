package com.bankingAppDemo.bankingAppDemo.service.Impl;

import com.bankingAppDemo.bankingAppDemo.entity.UserEntity;
import com.bankingAppDemo.bankingAppDemo.payload.response.BankResponse;
import com.bankingAppDemo.bankingAppDemo.repository.UserRepository;
import com.bankingAppDemo.bankingAppDemo.service.GeneralUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeneralUserServiceIMPL implements GeneralUserService {
    private final FileUploadServiceImpl fileUploadService;
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<BankResponse<String>> uploadProfilePics(Long id, MultipartFile multipartFile) {
        Optional<UserEntity> userEntityOptional =
                userRepository.findById(id);
        String fileUrl = null;
        try{
            if(userEntityOptional.isPresent()){
                fileUrl = fileUploadService.uploadFile(multipartFile);
                UserEntity userEntity = userEntityOptional.get();
                userEntity.setProfilePicture(fileUrl);
                userRepository.save(new UserEntity());
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(new BankResponse<>(
                "Upload Successfully",
                fileUrl
        ));
    }
}
