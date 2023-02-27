package gdsc.skhu.ourth.controller;

import gdsc.skhu.ourth.domain.dto.UserMissionDTO;
import gdsc.skhu.ourth.service.UserMissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserMissionController {

    private final UserMissionService userMissionService;

    @PostMapping("/usermission/all")
    public ResponseEntity<String> addUserMissionAllUser() {
        userMissionService.addUserMissionAllUser();
        return ResponseEntity.ok("모든 유저에게 주간 미션 추가 완료");
    }

    // 해당 유저에게 랜덤 미션 추가 - 유저
    @PostMapping("/usermission/add")
    public ResponseEntity<String> addUserMissionToUser(Principal principal) {
        userMissionService.addUserMissionToUser(principal);
        return ResponseEntity.ok(principal.getName() + " 유저에게 주간 미션 추가 완료");
    }

    // 모든 유저에게 주어진 주간 미션 삭제하기
    @DeleteMapping("/usermission")
    public ResponseEntity<String> deleteAllUserMission() {
        userMissionService.deleteAllUserMission();
        return ResponseEntity.ok("모든 유저의 미션 삭제 완료");
    }

    // 유저가 미션을 완료했을 때 완료로 바꾸고 포인트 획득, user_mission_id만 주면 됨 - 유저
    @PatchMapping("/usermission/clear")
    public ResponseEntity<String> successUserMission(@RequestBody UserMissionDTO dto) throws Exception {
        userMissionService.successUserMission(dto);
        return ResponseEntity.ok("해당 미션 성공");
    }


}
