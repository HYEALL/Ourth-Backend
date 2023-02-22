package gdsc.skhu.ourth.domain.dto;

import lombok.Builder;
import lombok.Data;

public class SchoolDTO {

    // 학교 순위 보여줄 때 사용
    @Data
    @Builder
    public static class School {

        private Long id;

        private String schoolName;

        private Long point;

    }

}
