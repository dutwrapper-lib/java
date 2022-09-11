package io.zoemeow.dutapi;

import com.google.gson.Gson;
import io.zoemeow.dutapi.objects.dutschoolyear.DUTSchoolYear;
import io.zoemeow.dutapi.objects.dutschoolyear.DUTSchoolYearItem;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static Long getCurrentTimeInUnix() {
        return System.currentTimeMillis();
    }

    public static DUTSchoolYearItem getDUTSchoolYear(Long unixTimestamp) {
        String content = Variables.DUT_WEEK_JSON;
        DUTSchoolYear itemList = new Gson().fromJson(content, DUTSchoolYear.class);
        return itemList.getCurrentSchoolYear(unixTimestamp);
//        URL resource = Thread.currentThread().getContextClassLoader().getResource("dut_week.json");
//        if (resource != null) {
//            String content = String.join("", Files.readAllLines(Paths.get(resource.toURI()), StandardCharsets.UTF_8));
//            DUTSchoolYear itemList = new Gson().fromJson(content, DUTSchoolYear.class);
//            return itemList.getCurrentSchoolYear(unixTimestamp);
//        } else throw new Exception("File 'dut_week.json' not found!");
    }

    public static String findFirstString(String test, String regex) {
        final Pattern patternDate = Pattern.compile(regex);
        final Matcher matcher = patternDate.matcher(test);
        if (matcher.find()) {
            return matcher.group(0);
        }
        else return null;
    }

    public static Long date2UnixTimestamp(String input) {
        // In format dd/MM/yyyy
        long date = 0L;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = LocalDateTime.of(
                    LocalDate.parse(input, formatter),
                    LocalTime.of(0, 0, 0)
            ).atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        } catch (Exception ignored) {

        }
        return date;
    }
}
