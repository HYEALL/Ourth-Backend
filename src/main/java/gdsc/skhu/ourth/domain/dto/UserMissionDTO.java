package gdsc.skhu.ourth.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gdsc.skhu.ourth.domain.Mission;
import gdsc.skhu.ourth.domain.User;
import gdsc.skhu.ourth.domain.UserMission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserMissionDTO {

    // 유저의 주간 미션을 추가할 때 사용하는 DTO

    @JsonIgnore
    private User user;

    @JsonIgnore
    private Mission mission;

    private long id;

    private String text;

    private Long point;

    private Boolean status;

    public UserMission toEntity() {
        return UserMission.builder()
                .id(id)
                .user(user)
                .mission(mission)
                .status(status)
                .build();
    }
}
