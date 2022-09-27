package com.ll.zzandi.controller;

import com.google.common.base.Strings;
import com.ll.zzandi.domain.Interest;
import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.TeamMate;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.UserDto;
import com.ll.zzandi.dto.study.MyStudyDto;
import com.ll.zzandi.dto.study.StudyListDto;
import com.ll.zzandi.enumtype.TeamMateDelegate;
import com.ll.zzandi.enumtype.TeamMateStatus;
import com.ll.zzandi.exception.ErrorType;
import com.ll.zzandi.exception.StudyException;
import com.ll.zzandi.exception.TeamMateException;
import com.ll.zzandi.exception.UserApplicationException;
import com.ll.zzandi.repository.InterestRepository;
import com.ll.zzandi.repository.TeamMateRepository;
import com.ll.zzandi.repository.UserRepository;
import com.ll.zzandi.service.StudyService;
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
    private final InterestRepository interestRepository;
    private final StudyService studyService;
    private final TeamMateRepository teamMateRepository;

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

    @GetMapping("/profileImage")
    public String getProfilePage(@AuthenticationPrincipal User user,Model model){
        model.addAttribute("user", user);
        return"/user/Profile-upload";
    }

    @PostMapping("/profileImage")
    @ResponseBody
    @Transactional
    public String updateProfileImage(@RequestParam("croppedImage") MultipartFile multipartFile, @AuthenticationPrincipal User user) throws IOException {
        userService.updateProfile(multipartFile, user);
        return  "1";
    }

    @GetMapping("/profile")
    public String showProfile(@RequestParam("userNickname") String userNickname, Model model) {
        User user = userRepository.findByUserNickname(userNickname)
            .orElseThrow(() -> new UserApplicationException(ErrorType.NOT_FOUND));

        List<Interest> interestList = interestRepository.findByUser(user);
        List<StudyListDto> recruitStudyList = studyService.findRecruitStudyList(user);
        List<StudyListDto> progressStudyList = studyService.findProgressStudyList(user);
        List<StudyListDto> completeStudyList = studyService.findCompleteStudyList(user);

        model.addAttribute("user", user);
        model.addAttribute("interestList", interestList);
        model.addAttribute("recruitStudyList", recruitStudyList);
        model.addAttribute("progressStudyList", progressStudyList);
        model.addAttribute("completeStudyList", completeStudyList);

        return "/user/profile";
    }

    @GetMapping("/mypage")
    public String showMyPage(@AuthenticationPrincipal User user, @RequestParam("userNickname") String userNickname, Model model) {

        User pageUser = userRepository.findByUserNickname(userNickname)
            .orElseThrow(() -> new UserApplicationException(ErrorType.NOT_FOUND));

        if (!user.getId().equals(pageUser.getId())) {
            throw new UserApplicationException(ErrorType.NOT_LEADER);
        }

        List<TeamMate> teamMateList = teamMateService.findAllByUser(user);
        model.addAttribute("user", pageUser);
        model.addAttribute("teamMateList", teamMateList);

        List<Interest> interestList = interestRepository.findByUser(pageUser);
        model.addAttribute("interestList", interestList);
        return "/user/mypage";
    }

    @GetMapping("/mystudy")
    public String showMyStudy(@AuthenticationPrincipal User user, Model model){

        List<Interest> interestList = interestRepository.findByUser(user);
        List<TeamMate> teamMateList = teamMateRepository.findByUserAndTeamMateStatus(
            user, TeamMateStatus.WAITING);
        List<MyStudyDto> waitingStudyList = studyService.findWaitingStudyList(teamMateList);

        model.addAttribute("user", user);
        model.addAttribute("interestList", interestList);
        model.addAttribute("teamMateList", teamMateList);
        model.addAttribute("waitingStudyList", waitingStudyList);

        return "/user/mystudy";
    }

    @GetMapping("/findWaitingStudyList")
    @ResponseBody
    public List<MyStudyDto> findWaitingStudyList(@AuthenticationPrincipal User user) {
        List<TeamMate> teamMateList = teamMateRepository.findByUserAndTeamMateStatus(
            user, TeamMateStatus.WAITING);
        List<MyStudyDto> studyList = studyService.findWaitingStudyList(teamMateList);
        return studyList;
    }

    @GetMapping("/findDelegateStudyList")
    @ResponseBody
    public List<MyStudyDto> findDelegateStudyList(@AuthenticationPrincipal User user) {
        List<TeamMate> teamMateList = teamMateRepository.findByUserAndTeamMateDelegate(
            user, TeamMateDelegate.WAITING);
        List<MyStudyDto> studyList = studyService.findWaitingStudyList(teamMateList);
        return studyList;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/check/id")
    @ResponseBody
    public Boolean checkUserId(@RequestParam("userid") String userid)  {
        System.out.println("연걸 성공"+userid);
        return userRepository.existsByUserId(userid);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/id")
    @ResponseBody
    public String updateUserId(@RequestParam("userid") String userid, @AuthenticationPrincipal User user)  {
        if(Strings.isNullOrEmpty(userid)) throw new RuntimeException();
        return userService.updateUserId(userid,user.getUserId());
    }
}