package gdsc.skhu.ourth.domain;

import gdsc.skhu.ourth.domain.dto.MissionDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Mission {

    // 모든 미션을 저장해두는 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Long id;

    private String text; // 미션 내용

    private Long point; // 지급 포인트

    @OneToMany(mappedBy = "mission")
    private List<UserMission> userMissions; // 어느 유저에게 주어졌는지

    public MissionDTO.Response toResponseDTO() {
        return MissionDTO.Response.builder()
                .id(id)
                .text(text)
                .point(point)
                .build();
    }

}
