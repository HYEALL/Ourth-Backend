package gdsc.skhu.ourth.service;

import gdsc.skhu.ourth.domain.School;
import gdsc.skhu.ourth.domain.User;
import gdsc.skhu.ourth.domain.dto.SchoolDTO;
import gdsc.skhu.ourth.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;

    // 학교 순위 조회
    public List<SchoolDTO.School> schoolRank() {
        List<School> schoolList = schoolRepository.findAll();
        List<SchoolDTO.School> dtoList = new ArrayList<>();

        // 모든 학교의 토탈 포인트를 계산
        for(School school : schoolList) {
            SchoolDTO.School dto = school.toSchoolDTO();

            // 해당 학교의 소속된 유저의 포인트를 모두 합산
            Long totalPoint = 0L;
            for(User user : school.getUsers()) {
                // 관리자 계정은 합산 X
                if(user.getId() == 1)
                    continue;
                totalPoint += user.getPoint();
            }

            // 만든 dto에 토탈 포인트를 추가 후 dtoList에 저장
            dto.setPoint(totalPoint);
            dtoList.add(dto);
        }

        // 토탈 포인트를 기준으로 내림차순 정렬
        dtoList.sort(new Comparator<SchoolDTO.School>() {
            @Override
            public int compare(SchoolDTO.School o1, SchoolDTO.School o2) {
                return (int)(o2.getPoint() - o1.getPoint());
            }
        });

        return dtoList;
    }

}
