package gdsc.skhu.ourth.controller;

import gdsc.skhu.ourth.domain.dto.UserInfoDTO;
import gdsc.skhu.ourth.domain.dto.UserMissionDTO;
import gdsc.skhu.ourth.service.UserMissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class UserMissionController {

    private final UserMissionService userMissionService;

    // 토큰 값으로 유저의 미션 리스트 확인 - 유저
    @GetMapping("/user/mission")
    public UserInfoDTO.missions missions(Principal principal) {
        return userMissionService.findByUserMissions(principal);
    }

    // 해당 유저에게 랜덤 미션 추가 - 유저
    @PostMapping("/usermission/add")
    public ResponseEntity<String> addUserMissionToUser(Principal principal) throws IllegalStateException {
        try {
            userMissionService.addUserMissionToUser(principal);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(principal.getName() + " 유저에게 주간 미션 추가 완료");
    }

    // 유저가 미션을 완료했을 때 완료로 바꾸고 포인트 획득, user_mission_id만 주면 됨 - 유저
    @PatchMapping("/usermission/clear")
    public ResponseEntity<String> successUserMission(@RequestBody UserMissionDTO.RequestSuccess dto) throws IllegalStateException {
        try {
            userMissionService.successUserMission(dto);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("해당 미션 성공");
    }

}