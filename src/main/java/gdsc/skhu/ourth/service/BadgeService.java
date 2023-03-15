package gdsc.skhu.ourth.service;

import gdsc.skhu.ourth.domain.Badge;
import gdsc.skhu.ourth.domain.User;
import gdsc.skhu.ourth.domain.UserMission;
import gdsc.skhu.ourth.domain.dto.BadgeDTO;
import gdsc.skhu.ourth.repository.BadgeRepository;
import gdsc.skhu.ourth.repository.UserMissionRepository;
import gdsc.skhu.ourth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static gdsc.skhu.ourth.util.DateUtil.*;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;
    private final UserMissionRepository userMissionRepository;

    // 뱃지 목록 반환
    public List<BadgeDTO.Response> badgeList(Principal principal) {
        // DB에서 해당 유저의 뱃지들을 가져옴
        List<Badge> badgeList = badgeRepository.findByUser(userRepository.findByEmail(principal.getName()).get());

        // 가져온 뱃지들을 DTO 형태로 변환 후 반환
        return badgeList.stream()
                .map(Badge::toResponseDTO).collect(Collectors.toList());
    }

    // 유저에게 뱃지 추가
    public void addBadge(Principal principal) throws IllegalStateException {
        // 현재 로그인 된 유저의 정보를 이용해 유저를 알아내고 dto에 추가함
        User user = userRepository.findByEmail(principal.getName()).get();
        BadgeDTO.RequestAddBadge dto = new Badge().toRequestAddDTO();
        dto.setUser(user);

        // 유저 미션
        List<UserMission> userMissions = userMissionRepository
                .findUserMissionByCreateDateBetweenAndUser(getCurSunday(), getCurSaturday(), user);

        // 주간 미션을 부여받지 않은 경우
        if(userMissions.isEmpty()) {
            throw new IllegalStateException("주간 미션이 없습니다.");
        }

        // 이번 주에 획득한 뱃지가 있을 경우 뱃지 획득 불가
        if(badgeRepository.findByCreateDateBetweenAndUser(getCurSunday(), getCurSaturday(), user).isPresent()) {
            throw new IllegalStateException("이번 주에 이미 뱃지를 획득했습니다.");
        }

        // 완료되지 않은 주간 미션이 있을 경우 뱃지 획득 불가
        for(UserMission userMission : userMissions) {
            if(!userMission.getStatus()) {
                throw new IllegalStateException("완료되지 않은 주간 미션이 존재합니다.");
            }
        }

        // Entity로 변환 후 DB에 저장
        badgeRepository.save(dto.toEntity());
    }

}
