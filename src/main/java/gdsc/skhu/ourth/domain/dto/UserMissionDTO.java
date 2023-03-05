package gdsc.skhu.ourth.domain.dto;

import gdsc.skhu.ourth.domain.Mission;
import gdsc.skhu.ourth.domain.User;
import gdsc.skhu.ourth.domain.UserMission;
import lombok.*;

import java.time.LocalDateTime;

public class UserMissionDTO {

    // 주간 미션 추가 요청 DTO
    @Data
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class RequestAddUserMission {

        private User user;

        private Mission mission;

        private Boolean status;

        public UserMission toEntity() {
            return UserMission.builder()
                    .user(user)
                    .mission(mission)
                    .status(status)
                    .build();
        }
    }

    // 주간 미션 완료 요청 DTO
    @Data
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class RequestSuccess {

        private Long id;

    }

    // 주간 미션 응답 DTO
    @Data
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long id;

        private LocalDateTime createDate;

        private String text;

        private Long point;

        private Boolean status;

    }

}
