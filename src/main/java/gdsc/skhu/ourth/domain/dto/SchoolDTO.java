package gdsc.skhu.ourth.domain.dto;

import lombok.Builder;
import lombok.Data;

public class SchoolDTO {

    // 학교 정보 응답 DTO
    @Data
    @Builder
    public static class Response {

        private Long id;

        private String schoolName; // 해당 학교의 이름

        private Long point; // 해당 학교에 소속된 유저들의 포인트 합계

        private Long ranking; // 순위 정보 ex) 1등, 2등

    }

}
