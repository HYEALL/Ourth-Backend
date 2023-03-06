package gdsc.skhu.ourth.domain.dto;

import gdsc.skhu.ourth.domain.Badge;
import gdsc.skhu.ourth.domain.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class BadgeDTO {

    // 뱃지 추가 요청 DTO
    @Data
    @Builder
    public static class RequestAddBadge {

        private User user;

        private String type;

        public Badge toEntity() {
            return Badge.builder()
                    .user(user)
                    .type(type)
                    .build();
        }

    }

    // 뱃지 응답 DTO
    @Data
    @Builder
    public static class Response {

        private LocalDateTime createTime;

        private String type;

    }
}
