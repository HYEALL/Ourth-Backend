package gdsc.skhu.ourth.repository;

import gdsc.skhu.ourth.domain.Badge;
import gdsc.skhu.ourth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    // 뱃지 목록
    List<Badge> findByUser(User user);

    // 해당 기간 사이의 획득한 뱃지 목록
    Optional<Badge> findByCreateDateBetweenAndUser(LocalDateTime start, LocalDateTime end, User user);

    // 유저의 뱃지 개수
    Long countByUser(User user);

}
