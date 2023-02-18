package gdsc.skhu.ourth.domain.dto;


import gdsc.skhu.ourth.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;

public class UserDTO {

    // 회원가입 요청 DTO
    @Getter
    @Builder
    @AllArgsConstructor
    public static class SignUp {
        @NotBlank(message = "이메일을 입력해주세요")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요")
        //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{4,20}$",
        //       message = "비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.") // 아직 안정함
        private String password;

        @NotBlank(message = "비밀번호(확인)를 입력해주세요")
        private String checkedPassword;

        @NotBlank(message = "닉네임을 입력해주세요")
        private String username;

        @NotBlank(message = "거주 지역을 입력해주세요")
        private String region;

        // 회원가입시 0포인트부터 시작
        private Long point;

        @Builder
        public User toEntity() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .region(region)
                    .roles(new ArrayList<>(Collections.singleton("USER")))
                    .username(username)
                    .point(0L)
                    .build();
        }
    }

    // 로그인 요청 DTO
    @Data
    public static class Login {

        private String email;

        private String password;

    }

}
