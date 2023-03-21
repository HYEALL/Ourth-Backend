package gdsc.skhu.ourth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailUtil {

    private final JavaMailSender mailSender;

    // Gmail smtp 서버를 이용한 메일 전송 메소드
    public void sendMail(String mailAddress, String link) {
        SimpleMailMessage message = new SimpleMailMessage();

        // 받는 사람
        message.setTo(mailAddress);

        // 메일 제목
        message.setSubject("Ourth 인증 메일입니다.");

        // 메일 내용
        message.setText("안녕하세요" + "\n\n" +
                "다음 링크를 통해 이메일 주소를 인증하세요." + "\n" +
                link + "\n\n" +
                "Ourth App의 인증을 요청하지 않았다면," + "\n" +
                "이 이메일을 무시하셔도 됩니다." + "\n\n" +
                "감사합니다.");

        // 메일 전송
        mailSender.send(message);
    }

}