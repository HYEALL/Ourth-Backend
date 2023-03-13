package gdsc.skhu.ourth.controller;

import gdsc.skhu.ourth.domain.dto.*;
import gdsc.skhu.ourth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 메인 페이지 - 누구나
    @GetMapping("/main")
    public String main() {
        return "메인 페이지";
    }

    // 로그인 - 누구나
    @PostMapping("/login")
    public TokenDTO login(@RequestBody UserDTO.RequestLogin dto) {
        return userService.login(dto);
    }

    // 로그아웃 - 유저
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, @RequestBody TokenDTO dto) {
        userService.logout(request, dto);
        return ResponseEntity.ok("로그아웃");
    }

    // 리프레쉬 - 유저
    @PostMapping("/refresh")
    public TokenDTO refresh(HttpServletRequest request, @RequestBody TokenDTO dto) throws Exception {
        return userService.refresh(request, dto);
    }

    // 회원가입 - 누구나
    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody UserDTO.RequestSignUp dto) throws IllegalStateException {
        try {
            userService.signUp(dto);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(" 회원가입 완료");
    }

    // 토큰 값으로 내 정보 확인 - 유저
    @GetMapping("/user")
    public UserInfoDTO userInfo(Principal principal) {
        return userService.userInfo(principal);
    }

}
