package gdsc.skhu.ourth.domain.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "user")
public class User implements UserDetails {
    // 유저 아이디
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    // 유저 비밀번호
    @Column(name = "password", nullable = false)
    private String password;

    // 유저 네임
    @Column(name = "username", nullable = false)
    private String username;

    // 지역
    @Column(name = "region", nullable = false)
    private String region;

    // 포인트
    @Column(name = "point")
    private Integer point;

    @OneToMany(mappedBy = "user")
    private List<Mission> missions = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @BatchSize(size = 1)
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
