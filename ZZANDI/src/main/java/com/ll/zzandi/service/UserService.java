package com.ll.zzandi.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.google.common.base.Strings;
import com.ll.zzandi.config.security.CustomUserDetailsService;
import com.ll.zzandi.config.security.UserContext;
import com.ll.zzandi.domain.File;
import com.ll.zzandi.domain.Interest;
import com.ll.zzandi.domain.TeamMate;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.UserDto;
import com.ll.zzandi.enumtype.TableType;
import com.ll.zzandi.exception.ErrorType;
import com.ll.zzandi.exception.UserApplicationException;
import com.ll.zzandi.repository.FileRepository;
import com.ll.zzandi.repository.InterestRepository;
import com.ll.zzandi.repository.StudyRepository;
import com.ll.zzandi.repository.UserRepository;
import com.ll.zzandi.util.aws.ImageUploadService;
import com.ll.zzandi.util.mail.EmailMessage;
import com.ll.zzandi.util.mail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {
    private final String DEFAULT_IMAGE_URL="https://zzandi.s3.ap-northeast-2.amazonaws.com/defaultImage.gif";
    private final Integer DEFAULT_USER_ZZANDI=15;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final EmailService emailService;
    private final InterestRepository interestRepository;

    private final ImageUploadService imageUploadService;

    private final FileRepository fileRepository;

    private final CustomUserDetailsService userDetailsService;
    private final StudyRepository studyRepository;

    @Transactional
    public User join(final UserDto.RegisterRequest registerRequest) {
            registerRequest.encodePassword(passwordEncoder);
            User newUser=userRepository.save(User.of(registerRequest));
            newUser.generateEmailCheckToken();
            sendSignUpConfirmEmail(newUser);
            newUser.updateImageUrl(DEFAULT_IMAGE_URL);
            newUser.updateUserZzandi(DEFAULT_USER_ZZANDI);

            for(String i : registerRequest.getInterests()){
                Interest interest=new Interest();
                interest.setInterest(i);
                interest.setUser(newUser);
                interestRepository.save(interest);
            }
            //TODO 이부분 하드 코등 추후 변경
        File file=File.builder()
                .fileName("defaultImage")
                .originalName("defaultImage")
                .fileExt("gif")
                .fileSize(0L)
                .fileUrl(DEFAULT_IMAGE_URL)
                .tableId(newUser.getId())
                .tableType(TableType.USER)
                .build();
            fileRepository.save(file);
            return newUser;
    }

    public void sendSignUpConfirmEmail(User user) {
        Context context = new Context();
        context.setVariable("link", "/check-email-token?token=" + user.getEmailCheckToken() +
                "&email=" + user.getUserEmail());
        context.setVariable("nickname", user.getUserNickname());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "ZZANDI 서비스를 사용하려면 링크를 클릭하세요.");
        context.setVariable("host", "http://localhost:8080/user");
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(user.getUserEmail())
                .subject("ZZANDI, 회원 가입 인증")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }
    @Transactional
    public void completeSignUp(User user) {
        user.completeSignUp();
        userRepository.save(user);
        login(user);
    }

    public void login(User account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserContext(account,List.of(new SimpleGrantedAuthority("ROLE_USER"))).getUser(),
                account.getUserPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
        System.out.println("TOKEN 생성완료");
    }

    @Transactional
    public void updateProfile(MultipartFile multipartFile,Long userUUID) throws IOException {
        User user=userRepository.findById(userUUID).orElseThrow(RuntimeException::new);
        if(user.getUserprofileUrl() != null){
            File file=fileRepository.findByTableId(userUUID);
            fileRepository.delete(file);
        }
        String originalName=multipartFile.getOriginalFilename();
        System.out.println("!!!!!!!!!!!!!!!오리지널"+originalName);
        String[] name=originalName.split("\\\\");
        final String ext = name[2].substring(name[2].lastIndexOf('.'));
        final String saveFileName = getUuid() + ext;
        String uploadUrl=imageUploadService.upload(saveFileName,multipartFile);
        File file=File.builder()
                .fileName(saveFileName)
                .originalName(name[2])
                .fileExt(ext)
                .fileSize(multipartFile.getSize())
                .fileUrl(uploadUrl)
                .tableId(userUUID)
                .tableType(TableType.USER)
                .build();
        file.deleteFileStatus();
        fileRepository.save(file);
        user.updateImageUrl(uploadUrl);
    }
    private static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String updateUserId(String updateId,String originId) {
        if(Strings.isNullOrEmpty(updateId)) throw new UserApplicationException(ErrorType.INTERNAL_SERVER_ERROR);
        User updateUser=userRepository.findByUserId(originId).orElseThrow(RuntimeException::new);
        updateUser.setUserId(updateId);
        userRepository.save(updateUser);
       login(updateUser);
       return updateUser.getUserId();
    }
    protected Authentication createNewAuthentication(Authentication currentAuth, String username) {
        UserDetails newPrincipal = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, currentAuth.getCredentials(), newPrincipal.getAuthorities());
        newAuth.setDetails(currentAuth.getDetails());
        return newAuth;
    }
    public String deleteUser(User user) {
        if(studyRepository.existsStudiesByUser(user)){
            return "error";
        }
        userRepository.deleteById(user.getId());
        //로그아웃 처리 해주기
        return "회원 탈퇴가 되었습니다.";
    }
}


