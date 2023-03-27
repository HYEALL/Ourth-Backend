package gdsc.skhu.ourth.domain;

import gdsc.skhu.ourth.domain.dto.UserInfoDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTime implements UserDetails {

    // 유저 정보를 저장하는 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, unique = true)
    private Long id; // 고유 번호

    @Column(unique = true)
    private String email; // 이메일

    private String password; // 비밀번호

    private String username; // 유저 이름

    @ManyToOne(targetEntity = School.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school; // 소속 학교

    private Long point; // 기여 포인트

    @OneToMany(mappedBy = "user")
    private List<UserMission> userMissions = new ArrayList<>(); // 유저 주간 미션 목록

    @OneToMany(mappedBy = "user")
    private List<Badge> badges = new ArrayList<>(); // 뱃지 목록

    // 권한 목록, @ElementCollection: 해당 필드가 컬렉션 객체임을 JPA에게 알려줌
    @ElementCollection(fetch = FetchType.EAGER) // 연관된 엔티티 즉시 로딩
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public UserInfoDTO.userInfo toInfoDTO() {
        return UserInfoDTO.userInfo.builder()
                .id(id)
                .email(email)
                .username(username)
                .school(school)
                .point(point)
                .build();
    }

    // 미션용
    public UserInfoDTO.missions toUserInfoMissionDTO() {
        return UserInfoDTO.missions.builder()
                .userMissions(userMissions.stream()
                        .map(UserMission::toResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    // 업적용
    public UserInfoDTO.achievement toAchievementDTO() {
        return UserInfoDTO.achievement.builder()
                .username(username)
                .point(point)
                .build();
    }

    // 계정의 권한 목록을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    // Principal 객체로 이메일 알아내기 위해 오버라이딩
    @Override
    public String getUsername() {
        return this.email;
    }

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

}
