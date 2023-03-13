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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    private final RedisTemplate<String, Object> redisTemplate;

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

        // 4. refreshToken을 redis에 저장 후 유효성 검증에 사용
        Long expiration = tokenProvider.getExpiration(tokenDTO.getRefreshToken()); // refreshToken의 유효기간

        redisTemplate.opsForValue()
                .set(tokenDTO.getRefreshToken(), "refreshToken", expiration, TimeUnit.MILLISECONDS);

        return tokenDTO;
    }

    // 로그아웃
    public void logout(HttpServletRequest request, TokenDTO dto) {
        // AccessToken 값
        String accessToken = tokenProvider.resolveToken(request);

        // 유효기간
        Long expiration = tokenProvider.getExpiration(accessToken);

        // 블랙 리스트 추가
        redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        // 기존 refreshToken 제거
        redisTemplate.delete(dto.getRefreshToken());
    }

    // 리프레시
    public TokenDTO refresh(HttpServletRequest request, TokenDTO dto) throws Exception {
        String refreshToken = dto.getRefreshToken();

        String isValidate = (String)redisTemplate.opsForValue().get(refreshToken);

        // redis에 유효한 refreshToken이 존재할 경우
        if(!ObjectUtils.isEmpty(isValidate)) {
            return tokenProvider.createAccessTokenByRefreshToken(request, refreshToken);
        }
        else {
            throw new Exception("refreshToken 없음");
        }
    }

    // 회원가입
    @Transactional
    public Long signUp(UserDTO.RequestSignUp dto) throws IllegalStateException {

        if(dto.getEmail() == null || dto.getEmail().length() == 0) {
            throw new IllegalStateException("이메일을 입력하지 않았습니다.");
        }

        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        if(dto.getPassword() == null || dto.getPassword().length() == 0) {
            throw new IllegalStateException("비밀번호를 입력하지 않았습니다.");
        }

        if(dto.getCheckedPassword() == null || dto.getCheckedPassword().length() == 0) {
            throw new IllegalStateException("확인 비밀번호를 입력하지 않았습니다.");
        }

        if(!dto.getPassword().equals(dto.getCheckedPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        if(dto.getSchoolName() == null || dto.getSchoolName().length() == 0) {
            throw new IllegalStateException("학교명을 입력하지 않았습니다.");
        }

        if(dto.getUsername() == null || dto.getUsername().length() == 0) {
            throw new IllegalStateException("유저 이름을 입력하지 않았습니다.");
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
