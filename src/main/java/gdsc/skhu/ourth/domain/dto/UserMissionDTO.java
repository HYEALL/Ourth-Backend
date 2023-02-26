package gdsc.skhu.ourth.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gdsc.skhu.ourth.domain.Mission;
import gdsc.skhu.ourth.domain.User;
import gdsc.skhu.ourth.domain.UserMission;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserMissionDTO {

    @JsonIgnore
    private User user;

    @JsonIgnore
    private Mission mission;

    private LocalDateTime createDate;

    private long id;

    private String text;

    private Long point;

    private Boolean status;

    @Data
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class AddUserMission { // 유저에게 미션 추가할 때 사용하는 dto

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
}
