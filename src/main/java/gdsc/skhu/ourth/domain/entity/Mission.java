package gdsc.skhu.ourth.domain.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Table(name = "mission")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Mission {
    // 미션 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    // 미션 내용
    @Column(name = "question")
    private String question;

    // User
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 미션 상태
    @Column(columnDefinition = "boolean default false")
    private Boolean status;
}
