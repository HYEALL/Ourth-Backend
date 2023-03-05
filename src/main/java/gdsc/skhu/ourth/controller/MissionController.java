package gdsc.skhu.ourth.controller;

import gdsc.skhu.ourth.domain.dto.MissionDTO;
import gdsc.skhu.ourth.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    // 미션 리스트 확인하기 - 관리자
    @GetMapping("/mission")
    public List<MissionDTO.Response> missionList() {
        return missionService.missionList();
    }

}
