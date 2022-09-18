package com.ll.zzandi.controller;

import com.ll.zzandi.domain.TeamMate;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.UserDto;
import com.ll.zzandi.exception.ErrorType;
import com.ll.zzandi.exception.UserApplicationException;
import com.ll.zzandi.repository.UserRepository;
import com.ll.zzandi.service.TeamMateService;
import com.ll.zzandi.service.UserService;
import com.ll.zzandi.util.validator.RegisterFormValidator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final RegisterFormValidator registerFormValidator;

    private final UserService userService;
    private final UserRepository userRepository;
    private final TeamMateService teamMateService;

    @InitBinder("registerRequest")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(registerFormValidator);
    }

    @GetMapping("/join")
    public String showSignForm(UserDto.RegisterRequest registerRequest, Model model) {
        model.addAttribute("interests", UserDto.RegisterRequest.getInterest());
        return "user/Sign-up";
    }


    @PostMapping("/join")
    public String join(Model model, @Valid UserDto.RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getFieldError());
            model.addAttribute("interests", UserDto.RegisterRequest.getInterest());
            return "user/Sign-up";
        }

        userService.join(registerRequest);
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        User user = userRepository.findByUserEmail(email);
        String view = "user/checked-email";
        if (user == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }

        if (!user.isValidToken(token)) {
            model.addAttribute("error", "wrong.token");
            return view;
        }

        userService.completeSignUp(user);
        model.addAttribute("numberOfUser", userRepository.count());
        model.addAttribute("nickname", user.getUserNickname());
        return view;
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
        @RequestParam(value = "exception", required = false) String exception, HttpServletRequest request, Model model) {

        String uri = request.getHeader("Referer");
        if (uri != null) {
            if (!uri.contains("/login")) {
                request.getSession().setAttribute("prevPage", uri);
            }
        }
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "/user/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response,
                authentication); // 세션 및 인증 객체 삭제
        }
        return "redirect:/user/login";
    }

    @GetMapping("/denied")
    public String accessDenied(@RequestParam(value = "exception", required = false) String exception, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("exception", exception);
        return "/user/denied";
    }

    @GetMapping("/profile")
    public String getProfilePage(@AuthenticationPrincipal User user,Model model){
        //TODO 이거 질문 하기 이로직을 너무 많이 사용할거 같은데 유저가 업데이트 되면 @Autuen 에서 가져오는 User정보도 업데이트를 하는 방법
        User currentUser=userRepository.findByUserId(user.getUserId()).orElseThrow(RuntimeException::new);
        model.addAttribute("user",currentUser);
        return"/user/Profile-upload";
    }

    @PostMapping("/profile")
    @ResponseBody
    @Transactional
    public String updateProfileImage(@RequestParam("croppedImage") MultipartFile multipartFile, @AuthenticationPrincipal User user) throws IOException {
        userService.updateProfile(multipartFile,user.getId());
        return  "1";
    }

    @GetMapping("/mypage")
    public String showMyPage(@AuthenticationPrincipal User user, @RequestParam("userNickname") String userNickname, Model model) {
        User currentUser = userRepository.findByUserId(user.getUserId()).orElseThrow(() -> new UserApplicationException(
            ErrorType.NOT_FOUND));

        User pageUser = userRepository.findByUserNickname(userNickname).orElseThrow(() -> new UserApplicationException(
            ErrorType.NOT_FOUND));

        List<TeamMate> teamMateList = teamMateService.findAllByUser(pageUser);

        model.addAttribute("currentUser", currentUser);
        //여기서 user랑 currentuser랑 쓰임이 다른가요??
        model.addAttribute("user", pageUser);
        model.addAttribute("teamMateList", teamMateList);
        return "/user/mypage";
    }

    @GetMapping("/mystudy")
    public String showMyStudy(@AuthenticationPrincipal User user, Model model){
        User currentUser = userRepository.findByUserId(user.getUserId()).orElseThrow(() -> new UserApplicationException(
                ErrorType.NOT_FOUND));
        User pageUser = userRepository.findByUserNickname(user.getUserNickname()).orElseThrow(() -> new UserApplicationException(
                ErrorType.NOT_FOUND));
        List<TeamMate> teamMateList = teamMateService.findAllByUser(currentUser);
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("user", pageUser);
        model.addAttribute("teamMateList", teamMateList);
        return "/user/mystudy";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/id")
    @ResponseBody
    public Boolean checkUserId(@RequestParam("userid") String userid)  {
        System.out.println("연걸 성공"+userid);
        return userRepository.existsByUserId(userid);
    }
}