package gdsc.skhu.ourth.util;

import java.time.LocalDateTime;
import java.util.Calendar;

public class DateUtil {

    // 오늘 기준으로 이번 주 일요일 - 시작
    public static LocalDateTime getCurSunday(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1; // month 0부터 시작
        int date = c.get(Calendar.DATE);
        return LocalDateTime.of(year, month, date, 0, 0,0);
    }

    // 오늘 기준으로 이번 주 토요일 - 끝
    public static LocalDateTime getCurSaturday(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;  // month 0부터 시작
        int date = c.get(Calendar.DATE);
        return LocalDateTime.of(year, month, date, 23, 59,59);
    }

}
