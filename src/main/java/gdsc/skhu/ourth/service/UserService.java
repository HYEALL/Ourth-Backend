package gdsc.skhu.ourth.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import gdsc.skhu.ourth.domain.School;
import gdsc.skhu.ourth.domain.User;
import gdsc.skhu.ourth.domain.UserMission;
import gdsc.skhu.ourth.domain.dto.*;
import gdsc.skhu.ourth.jwt.TokenProvider;
import gdsc.skhu.ourth.repository.SchoolRepository;
import gdsc.skhu.ourth.repository.UserMissionRepository;
import gdsc.skhu.ourth.repository.UserRepository;
import gdsc.skhu.ourth.util.MailUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    private final RedisTemplate<String, Object> redisTemplate;
    private final MailUtil mailUtil;

    // 로그인
    public ResponseEntity<ResponseDTO> login(UserDTO.RequestLogin dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        // 응답으로 보낼 DTO
        ResponseDTO responseDTO = new ResponseDTO();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        // 임시로 관리자 선별하기, 만들어둔 관리자 계정은 비밀번호 암호화가 안되어있음 그래서 그냥 passwordEncoder 과정 넘김, 추후 수정 예정
        if(email.equals("kim@naver.com")) {

        }
        else {
            Optional<User> user = userRepository.findByEmail(email);

            // 유저가 아닌 경우
            if(user.isEmpty()) {
                responseDTO.setStatus("BAD_REQUEST");
                responseDTO.setMessage("아이디와 비밀번호를 확인해주세요.");
                return new ResponseEntity<>(responseDTO, header, HttpStatus.BAD_REQUEST);
            }

            // 비밀번호가 다를 때
            if(passwordEncoder.matches(password, user.get().getPassword())) {
                password = user.get().getPassword();
            } else {
                responseDTO.setStatus("BAD_REQUEST");
                responseDTO.setMessage("아이디와 비밀번호를 확인해주세요.");
                return new ResponseEntity<>(responseDTO, header, HttpStatus.BAD_REQUEST);
            }
        }

        try {
            // firebase 이메일 인증을 완료했는지 확인
            if(!FirebaseAuth.getInstance().getUserByEmail(email).isEmailVerified()) {
                responseDTO.setStatus("BAD REQUEST");
                responseDTO.setMessage("이메일 인증을 완료하지 않았습니다.");
                return new ResponseEntity<>(responseDTO, header, HttpStatus.BAD_REQUEST);
            }
        } catch (FirebaseAuthException e) { // FirebaseAuthError
            responseDTO.setStatus("BAD REQUEST");
            responseDTO.setMessage("Firebase Error");
            return new ResponseEntity<>(responseDTO, header, HttpStatus.BAD_REQUEST);
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

        // 회원가입 완료
        responseDTO.setStatus("OK");
        responseDTO.setMessage("로그인을 완료했습니다.");
        responseDTO.setData(tokenDTO);
        return new ResponseEntity<>(responseDTO, header, HttpStatus.OK);
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
    public ResponseEntity<ResponseDTO> refresh(HttpServletRequest request, TokenDTO dto){
        String refreshToken = dto.getRefreshToken();

        String isValidate = (String)redisTemplate.opsForValue().get(refreshToken);

        // 응답으로 보낼 DTO
        ResponseDTO responseDTO = new ResponseDTO();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        // redis에 유효한 refreshToken이 존재할 경우
        if(!ObjectUtils.isEmpty(isValidate)) {
            responseDTO.setStatus("OK");
            responseDTO.setMessage("AccessToken 재발급을 완료했습니다.");
            responseDTO.setData(tokenProvider.createAccessTokenByRefreshToken(request, refreshToken));
            return new ResponseEntity<>(responseDTO, header, HttpStatus.OK);
        }
        else { // refresh 할 수 없는 경우
            responseDTO.setStatus("BAD REQUEST");
            responseDTO.setMessage("RefreshToken이 유효하지 않거나, 존재하지 않습니다.");
            return new ResponseEntity<>(responseDTO, header, HttpStatus.OK);
        }
    }

    // 회원가입
    @Transactional
    public Long signUp(UserDTO.RequestSignUp dto) throws IllegalStateException, FirebaseAuthException {

        if(dto.getEmail() == null || dto.getEmail().length() == 0) {
            throw new IllegalStateException("이메일을 입력하지 않았습니다.");
        }

        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        if(dto.getPassword() == null || dto.getPassword().length() == 0) {
            throw new IllegalStateException("비밀번호를 입력하지 않았습니다.");
        }

        if(dto.getPassword().length() < 8) {
            throw new IllegalStateException("비밀번호는 8글자 이상이어야 합니다.");
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

        // Firebase user create
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(dto.getEmail())
                .setEmailVerified(false)
                .setDisplayName(dto.getUsername())
                .setPassword(dto.getPassword())
                .setDisplayName(dto.getUsername())
                .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

        // Firebase Authentication Link create
        try {
            // 인증 링크 생성
            String link = FirebaseAuth.getInstance().generateEmailVerificationLink(userRecord.getEmail());

            // 메일 보내기
            mailUtil.sendMail(userRecord.getEmail(), link);

        } catch (FirebaseAuthException e) {
            System.out.println("링크 생성 에러: " + e.getMessage());
        }

        // DB에 저장
        School school = schoolRepository.findBySchoolName(dto.getSchoolName()).get();
        dto.setSchool(school);
        User user = userRepository.save(dto.toEntity());
        user.encodePassword(passwordEncoder);

        return user.getId();
    }

    // 유저의 정보를 보여줌
    public UserInfoDTO.userInfo userInfo(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).get();
        UserInfoDTO.userInfo dto = user.toInfoDTO();

        // 이번 주 월요일부터 일요일까지의 유저미션을 가져옴
        List<UserMission> userMissions = userMissionRepository
                .findUserMissionByCreateDateBetweenAndUser(getCurSunday(), getCurSaturday(), user);

        // UserInfoDTO에 주간 미션 존재 여부 추가
        dto.setMissionPresence(!userMissions.isEmpty());

        // UserInfoDTO에 남은 주간 미션 개수 추가
        int missionCount = 0;
        for(UserMission userMission : userMissions) {
            if(!userMission.getStatus()) {
                missionCount++;
            }
        }
        dto.setMissionCount(missionCount);

        // UserInfoDTO에 학교 이름 추가
        dto.setSchoolName(dto.getSchool().getSchoolName());

        return dto;
    }

}