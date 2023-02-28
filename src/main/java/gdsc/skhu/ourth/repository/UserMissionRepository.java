package gdsc.skhu.ourth.repository;

import gdsc.skhu.ourth.domain.User;
import gdsc.skhu.ourth.domain.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {

    List<UserMission> findUserMissionByCreateDateBetweenAndUser(LocalDateTime start, LocalDateTime end, User user);

}
