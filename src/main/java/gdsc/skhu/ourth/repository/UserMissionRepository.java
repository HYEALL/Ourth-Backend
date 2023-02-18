package gdsc.skhu.ourth.repository;

import gdsc.skhu.ourth.domain.Mission;
import gdsc.skhu.ourth.domain.User;
import gdsc.skhu.ourth.domain.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {

    // 해당 유저의 특정 주간 미션을 완료
    Optional<UserMission> findUserMissionByUserAndMission(User user, Mission mission);

    // 해당 유저의 주간 미션이 지금 몇 개인지?
    Long countUserMissionByUser(User user);

}
