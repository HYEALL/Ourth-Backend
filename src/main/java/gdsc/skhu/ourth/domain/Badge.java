package gdsc.skhu.ourth.domain;

import gdsc.skhu.ourth.domain.dto.BadgeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Badge extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // N:1, 유저

    public BadgeDTO.Response toResponseDTO() {
        return BadgeDTO.Response.builder()
                .createTime(getCreateDate())
                .build();
    }

    public BadgeDTO.RequestAddBadge toRequestAddDTO() {
        return BadgeDTO.RequestAddBadge.builder()
                .build();
    }

}
