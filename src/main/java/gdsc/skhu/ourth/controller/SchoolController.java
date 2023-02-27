package gdsc.skhu.ourth.controller;

import gdsc.skhu.ourth.domain.dto.SchoolDTO;
import gdsc.skhu.ourth.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SchoolController {

    private final SchoolService schoolService;

    // 학교 순위 조회
    @GetMapping("/rank/school")
    public List<SchoolDTO.School> schoolRank() {
        return schoolService.schoolRank();
    }

}
