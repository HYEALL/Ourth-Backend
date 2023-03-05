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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;

    // 학교 순위 조회
    public List<SchoolDTO.Response> schoolRank() {
        List<School> schoolList = schoolRepository.findAll();
        List<SchoolDTO.Response> dtoList = new ArrayList<>();

        // 모든 학교의 토탈 포인트를 계산
        for(School school : schoolList) {
            SchoolDTO.Response dto = school.toResponseDTO();

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
        dtoList.sort(new Comparator<SchoolDTO.Response>() {
            @Override
            public int compare(SchoolDTO.Response o1, SchoolDTO.Response o2) {
                return (int)(o2.getPoint() - o1.getPoint());
            }
        });

        // 순위 매기기
        Long temp = dtoList.get(0).getPoint(); // 이전 DTO의 점수 임시 저장 변수
        Long ranking = 1L; // 순위 정보
        long sameCount = -1L; // 현재 동점 학교 개수
        for(SchoolDTO.Response dto : dtoList) {
            if(Objects.equals(temp, dto.getPoint())) {
                dto.setRanking(ranking);
                sameCount++;
            }
            else {
                ranking += sameCount; // 앞에 동점 학교만큼 순위 증가
                dto.setRanking(++ranking); // 순위 정보 추가
                sameCount = 0L; // 동점 학교 초기화
                temp = dto.getPoint(); // 이전 DTO 새로 저장
            }
        }

        return dtoList;
    }

}
