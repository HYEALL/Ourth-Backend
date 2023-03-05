package gdsc.skhu.ourth.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gdsc.skhu.ourth.domain.School;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfoDTO {

    // 유저의 정보를 전달하는 DTO

    private Long id;

    private String email;

    private String username;

    @JsonIgnore
    private School school;

    private String schoolName;

    private Long point;

    private List<UserMissionDTO.Response> userMissions;

    @Data
    @Builder
    public static class Badge {

        private Long id;

        private String email;

        private String username;

        private String schoolName;

        private Long point;

        private List<BadgeDTO> badges;
    }
}
