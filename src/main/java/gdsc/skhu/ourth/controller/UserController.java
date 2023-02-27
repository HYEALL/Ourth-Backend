package gdsc.skhu.ourth.controller;

import gdsc.skhu.ourth.domain.dto.*;
import gdsc.skhu.ourth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    // 메인 페이지 - 누구나
    @GetMapping("/main")
    public String main() {
        return "메인 페이지";
    }

    // 로그인 - 누구나
    @PostMapping("/login")
    public TokenDTO login(@RequestBody UserDTO.Login dto) {
        return userService.login(dto);
    }

    // 회원가입 - 누구나
    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody UserDTO.SignUp dto) throws Exception {
        return ResponseEntity.ok(userService.signUp(dto).toString() + " 회원가입 완료");
    }

    // 토큰 값으로 내 정보 확인 - 유저
    @GetMapping("/user")
    public UserInfoDTO userInfo(Principal principal) {
        return userService.userInfo(principal);
    }

}
