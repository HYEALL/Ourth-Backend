package gdsc.skhu.ourth.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MissionDTO {

    // 관리자용, 새로운 미션을 추가할 때 사용하는 DTO

    private Long id;

    private String text;

    private Long point;

}
