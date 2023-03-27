package gdsc.skhu.ourth.service;

import gdsc.skhu.ourth.domain.Mission;
import gdsc.skhu.ourth.domain.User;
import gdsc.skhu.ourth.domain.UserMission;
import gdsc.skhu.ourth.domain.dto.UserInfoDTO;
import gdsc.skhu.ourth.domain.dto.UserMissionDTO;
import gdsc.skhu.ourth.repository.BadgeRepository;
import gdsc.skhu.ourth.repository.MissionRepository;
import gdsc.skhu.ourth.repository.UserMissionRepository;
import gdsc.skhu.ourth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// 오늘을 기준으로 이번 주의 시작과 끝을 알아내기 위함
import static gdsc.skhu.ourth.util.DateUtil.*;


@Service
@RequiredArgsConstructor
public class UserMissionService {

    private final UserMissionRepository userMissionRepository;
    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final BadgeRepository badgeRepository;

    // 해당 유저의 주간 미션 목록 반환
    public UserInfoDTO.missions findByUserMissions(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).get();

        UserInfoDTO.missions missions = user.toUserInfoMissionDTO();

        // 주간 미션
        missions.setUserMissions(userMissionRepository
                .findUserMissionByCreateDateBetweenAndUser(getCurSunday(), getCurSaturday(), user)
                .stream().map(UserMission::toResponseDTO)
                .collect(Collectors.toList()));

        // 뱃지 획득 여부
        missions.setCurrentBadge(badgeRepository
                .findByCreateDateBetweenAndUser(getCurSunday(), getCurSaturday(), user).isPresent());

        return missions;
    }

    // 해당 유저에게 주간 미션 추가
    public void addUserMissionToUser(Principal principal) throws IllegalStateException {
        User user = userRepository.findByEmail(principal.getName()).get();
        List<Mission> missions = missionRepository.findAll();

        // 이미 이번 주에 부여된 미션이 있으면 추가 불가능
        if(userMissionRepository
                .findUserMissionByCreateDateBetweenAndUser(getCurSunday(), getCurSaturday(), user)
                .size() != 0) {
            throw new IllegalStateException("이미 이번 주 미션이 존재합니다.");
        };

        // 1부터 미션 총 개수만큼 수를 리스트 num에 넣고 shuffle <- 랜덤
        ArrayList<Long> num = new ArrayList<>();
        for(int i = 1; i <= missions.size(); i++) {
            num.add((long) i);
        }
        Collections.shuffle(num);

        // 해당 유저에게 미션 4개 추가
        for(int i = 0; i < 4; i++) {
            Mission mission = missionRepository.findById(num.get(i)).get();
            UserMissionDTO.RequestAddUserMission dto = new UserMissionDTO.RequestAddUserMission();
            dto.setUser(user);
            dto.setMission(mission);
            dto.setStatus(false);
            userMissionRepository.save(dto.toEntity());
        }

    }

    // 해당 유저의 해당 미션 성공
    public void successUserMission(UserMissionDTO.RequestSuccess dto) throws IllegalStateException {
        // 해당 주간 미션 완료
        UserMission userMission = userMissionRepository.findById(dto.getId()).get();
        if(!userMission.getStatus()) {
            userMission.setStatus(true);
        }
        else {
            throw new IllegalStateException("이미 완료한 미션입니다.");
        }

        // 완료한 미션의 포인트만큼 유저의 포인트 증가
        User user = userRepository.findById(userMission.getUser().getId()).get();
        user.setPoint(user.getPoint() + userMission.getMission().toResponseDTO().getPoint());
        userRepository.save(user);
    }

}
