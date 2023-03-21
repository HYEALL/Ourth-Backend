package gdsc.skhu.ourth.controller;

import gdsc.skhu.ourth.domain.dto.BadgeDTO;
import gdsc.skhu.ourth.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    // 뱃지 목록 확인하기 - 유저
    @GetMapping("/badge/list")
    public List<BadgeDTO.Response> badgeList(Principal principal) {
        return  badgeService.badgeList(principal);
    }

    // 뱃지 추가하기 - 유저
    @PostMapping("/badge/add")
    public ResponseEntity<String> addBadge(Principal principal) throws IllegalStateException {
        try {
            badgeService.addBadge(principal);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("뱃지 추가 완료");
    }

}