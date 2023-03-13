package gdsc.skhu.ourth.domain.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import gdsc.skhu.ourth.domain.School;
import gdsc.skhu.ourth.domain.User;
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
    public static class RequestSignUp {

        private String email;

        private String password;

        private String checkedPassword;

        private String username;

        @JsonIgnore
        private School school;

        private String schoolName;

        // 회원가입시 0포인트부터 시작
        private Long point;

        @Builder
        public User toEntity() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .school(school)
                    .roles(new ArrayList<>(Collections.singleton("USER")))
                    .username(username)
                    .point(0L)
                    .build();
        }

        public void setSchool(School school) {
            this.school = school;
        }

    }

    // 로그인 요청 DTO
    @Data
    public static class RequestLogin {

        private String email;

        private String password;

    }

}
