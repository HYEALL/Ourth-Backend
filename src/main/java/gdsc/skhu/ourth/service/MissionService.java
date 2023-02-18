package gdsc.skhu.ourth.service;

import gdsc.skhu.ourth.domain.Mission;
import gdsc.skhu.ourth.domain.dto.MissionDTO;
import gdsc.skhu.ourth.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    // 미션 리스트
    public List<MissionDTO> missionList() {
        return missionRepository.findAll().stream()
                .map(Mission::toDTO)
                .collect(Collectors.toList());
    }

}
