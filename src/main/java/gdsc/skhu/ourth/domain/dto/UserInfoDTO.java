package gdsc.skhu.ourth.domain.dto;

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

    private String region;

    private Long point;

    private List<UserMissionDTO> userMissions;

}
