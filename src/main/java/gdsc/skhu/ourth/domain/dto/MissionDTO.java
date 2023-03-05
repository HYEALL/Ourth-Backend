package gdsc.skhu.ourth.domain.dto;

import lombok.Builder;
import lombok.Data;

public class MissionDTO {

    // 미션 응답 DTO
    @Data
    @Builder
    public static class Response {

        private Long id;

        private String text;

        private Long point;

    }

}
