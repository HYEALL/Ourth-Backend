package gdsc.skhu.ourth.domain;

import gdsc.skhu.ourth.domain.dto.UserMissionDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserMission extends BaseTime {

    // 유저에게 주어진 미션과 완료 여부를 저장하는 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_mission_id")
    private Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // N:1, 유저

    @ManyToOne(targetEntity = Mission.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission; // 미션 내용

    @Column(columnDefinition = "boolean default false")
    private Boolean status; // 미션 완료 여부

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public UserMissionDTO toDTO() {
        return UserMissionDTO.builder()
                .id(id)
                .user(user)
                .mission(mission)
                .status(status)
                .createDate(getCreateDate())
                .text(mission.toDTO().getText())
                .point(mission.toDTO().getPoint())
                .build();
    }
}
