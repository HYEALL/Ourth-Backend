package gdsc.skhu.ourth.service;

import gdsc.skhu.ourth.domain.School;
import gdsc.skhu.ourth.domain.User;
import gdsc.skhu.ourth.domain.UserMission;
import gdsc.skhu.ourth.domain.dto.*;
import gdsc.skhu.ourth.jwt.TokenProvider;
import gdsc.skhu.ourth.repository.BadgeRepository;
import gdsc.skhu.ourth.repository.SchoolRepository;
import gdsc.skhu.ourth.repository.UserMissionRepository;
import gdsc.skhu.ourth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static gdsc.skhu.ourth.util.DateUtil.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final UserMissionRepository userMissionRepository;
    private final BadgeRepository badgeRepository;

    // 로그인
    public TokenDTO login(UserDTO.RequestLogin dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        // 임시로 관리자 선별하기, 만들어둔 관리자 계정은 비밀번호 암호화가 안되어있음 그래서 그냥 passwordEncoder 과정 넘김, 추후 수정 예정
        if(email.equals("kim@naver.com")) {

        }
        else {
            if(passwordEncoder.matches(password, userRepository.findByEmail(email).get().getPassword())) {
                password = userRepository.findByEmail(email).get().getPassword();
            }
        }

        // 1. email, password 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        // 2. 실제 검증 단계
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증된 정보를 기반으로 JWT 토큰 생성 후 반환
        TokenDTO tokenDTO = tokenProvider.createToken(authentication);
        return tokenDTO;
    }

    // 회원가입
    @Transactional
    public Long signUp(UserDTO.RequestSignUp dto) throws Exception {

        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if(!dto.getPassword().equals(dto.getCheckedPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        if(dto.getSchoolName().isEmpty()) {
            throw new Exception("거주 지역을 입력하지 않았습니다.");
        }

        School school = schoolRepository.findBySchoolName(dto.getSchoolName()).get();
        dto.setSchool(school);
        User user = userRepository.save(dto.toEntity());
        user.encodePassword(passwordEncoder);

        return user.getId();
    }

    // 유저의 정보를 보여줌
    public UserInfoDTO userInfo(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).get();
        UserInfoDTO dto = user.toInfoDTO();

        // 이번 주 월요일부터 일요일까지의 유저미션을 가져옴
        List<UserMission> userMissions = userMissionRepository
                .findUserMissionByCreateDateBetweenAndUser(getCurSunday(), getCurSaturday(), user);

        // UserInfoDTO에 주간 미션들 추가
        dto.setUserMissions(userMissions.stream()
                .map(UserMission::toResponseDTO).collect(Collectors.toList()));

        // UserInfoDTO에 학교 이름 추가
        dto.setSchoolName(dto.getSchool().getSchoolName());

        // UserInfoDTO에 이번 주 뱃지 유무
        dto.setCurrentBadge(!(badgeRepository
                .findByCreateDateBetweenAndUser(getCurSunday(), getCurSaturday(), user).isEmpty()));

        return dto;
    }

}
