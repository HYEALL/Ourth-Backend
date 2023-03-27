package gdsc.skhu.ourth.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import gdsc.skhu.ourth.repository.UserRepository;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class ScheduleUtil {

    private UserRecord userRecord;
    private UserRepository userRepository;

    // 회원가입 취소 작업
    Runnable cancelSignup = () -> {
        // 24시간 이후에도 이메일 인증을 완료하지 않았다면, 유저 삭제
        if(!userRecord.isEmailVerified()) {
            try {
                FirebaseAuth.getInstance().deleteUser(userRecord.getUid());
                userRepository.deleteByEmail(userRecord.getEmail());
            } catch (FirebaseAuthException e) {
                throw new RuntimeException(e);
            }
        }
    };

    // 작업 실행하기
    public void executeTask(UserRecord userRecord, UserRepository userRepository) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        TaskScheduler scheduler = new ConcurrentTaskScheduler(executor);
        this.userRecord = userRecord;
        this.userRepository = userRepository;

        // 24시간 이후 cancelSignup 실행
        scheduler.schedule(cancelSignup,
                Instant.ofEpochSecond(Instant.now().getEpochSecond() + 3600 * 24));
    }
}
