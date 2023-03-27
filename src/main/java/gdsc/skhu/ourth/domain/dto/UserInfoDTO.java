package gdsc.skhu.ourth.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gdsc.skhu.ourth.domain.School;
import lombok.*;

import java.util.List;

public class UserInfoDTO {

    @Data
    @Builder
    public static class userInfo {  // 유저의 정보를 전달하는 DTO

        private Long id;

        private String email; // 유저 이메일

        private String username; // 유저 이름

        @JsonIgnore
        private School school; // 유저의 소속 학교

        private String schoolName; // 유저의 소속 학교 이름

        private Long point; // 유저의 기여 포인트

        private Boolean missionPresence; // 유저의 주간 미션 보유 여부

        private Integer missionCount; // 유저 미션이 있다면 몇 개 있는지

    }

    @Data
    @Builder
    @Getter
    @Setter
    public static class missions {

        private Boolean currentBadge; // 최근(이번 주) 획득한 뱃지 유무

        private List<UserMissionDTO.Response> userMissions; // 유저의 주간 미션 목록

    }

    @Data
    @Builder
    @Getter
    @Setter
    public static class achievement {

        private String username; // 유저 이름

        private Long point; // 포인트

        private Long badgeCount; // 획득한 뱃지 수

        private List<UserMissionDTO.Response> userMissions; // 유저의 지금까지 완료한 미션 목록

    }

}
