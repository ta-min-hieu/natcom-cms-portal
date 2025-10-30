package com.ringme.common;

import com.google.gson.JsonArray;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.enums.sys.ListAjaxType;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.text.DecimalFormat;

@Log4j2
public final class Helper {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static boolean isCreateForm(Object object) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField("id");
        field.setAccessible(true);
        return (Long) field.get(object) == null;
    }

    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static String getTimeNow(){
        try {
            LocalDate currentDate = LocalDate.now();
            return currentDate.getYear() + "/" + currentDate.getMonthValue() + "/" + currentDate.getDayOfMonth();
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return "error";
    }

    public static String[] getTimeNowV2(){
        try {
            LocalDate currentDate = LocalDate.now();
            String[] results = new String[4];
            results[0] = currentDate.getYear() + "/" + currentDate.getMonthValue() + "/" + currentDate.getDayOfMonth();
            results[1] = String.valueOf(currentDate.getYear());
            results[2] = String.valueOf(currentDate.getMonthValue());
            results[3] = String.valueOf(currentDate.getDayOfMonth());
            return results;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

    public static String processStringNullEmptyToInt(String input){
        if(input == null || input.equals(""))
            input = "0";
        return input;
    }

    public static String processStringSearch(String input) {
        if(StringUtils.isNotEmpty(input))
            return input.trim();
        return null;
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return formatter.format(date);
    }

    public static String formatNumber(double input) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(input);
    }

    public static String normalizeNumber(String number) {
        if(number == null || number.equals(""))
            return  "0";
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(Long.parseLong(number));
    }
    public static String normalizeNumber(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(number);
    }
    public static String convertMsToMinutes(Long input) {
        if(input == null)
            input = 0L;
        double minutes = (double) input / 60000;
        return normalizeNumber(minutes);
    }
    public static Integer convertStringToInt(String input){
        try {
            if (input != null) {
                if (input.trim().equals(""))
                    input = null;
                else
                    input = input.trim().replaceAll("\s+", "");
            }
        } catch (Exception e) {
            log.error("Error|"+e.getMessage(),e);
        }
        if(input == null)
            return null;
        return Integer.parseInt(input);
    }

    public static String convertToSlug(String input) {
        if(input == null)
            return null;
        String slug = input.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
        slug = slug.replaceAll("\s+", "-");
        return slug;
    }

    public static String[] reportDate(String input) {
        String[] parts = new String[2];
        try {
            if (input == null || input.equals("")) {
                parts[0] = null;
                parts[1] = null;
            } else {
                parts = input.split("\\ - ");
                parts[0] += " 00:00:00";
                parts[1] += " 23:59:59";
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return parts;
    }

    public static String[] reportDateV2(String input) {
        String[] parts = new String[2];
        try {
            if (input == null || input.equals("")) {
                parts[0] = null;
                parts[1] = null;
            } else {
                parts = input.split("\\ - ");
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return parts;
    }

    public static String[] reportMonth(String input) {
        String[] parts = new String[2];
        try {
            if (input == null || input.equals("")) {
                parts[0] = null;
                parts[1] = null;
            } else {
                parts = input.split(" - ");
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return parts;
    }

    public static Path getPathByTime() {
        LocalDate currentDate = LocalDate.now();
        return Paths.get(String.valueOf(currentDate.getYear()), String.valueOf(currentDate.getMonthValue()), String.valueOf(currentDate.getDayOfMonth()));
    }
    public static List<AjaxSearchDto> listAjax(List<String[]> input, int type) {
        List<AjaxSearchDto> listAjax = new ArrayList<>();
        for (String[] strings : input) {
            AjaxSearchDto dto = new AjaxSearchDto();
            dto.setId(strings[0]);
            if(type == ListAjaxType.ID_AND_TEXT.getType())
                dto.setText(strings[0] + " - " + strings[1]);
            else
                dto.setText(strings[1]);
            if (strings.length > 2) {
                dto.setServiceId(strings[2]);
            }
            listAjax.add(dto);
        }
        return listAjax;
    }

    public static List<AjaxSearchDto> listAjaxV2(List<String[]> input, int type) {
        List<AjaxSearchDto> listAjax = new ArrayList<>();
        for (String[] strings : input) {
            AjaxSearchDto dto = new AjaxSearchDto();
            dto.setId(strings[0]);
            if(type == 0)
                dto.setText(strings[0] + " - " + strings[1]);
            else
                dto.setText(strings[1]);
            listAjax.add(dto);
        }
        return listAjax;
    }

    public static void RunShellCommand(String input) {
        log.info("--------------------------++++=========");
        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(new File(System.getProperty("user.home")));
            if (isWindows)
                builder.command("cmd.exe", "/c", input);
            else
                builder.command("sh", "-c", input);
            builder.start();
        } catch (Exception e) {
            log.error("Error Exception: " + e.getMessage(), e);
        }
    }
    public static String getMediaUri(String input) {
        String result = "";
        int lastIndex = input.lastIndexOf("/");
        if (lastIndex != -1) {
            result = input.substring(lastIndex + 1);
            log.info("RequestUrii|" + result);
        } else {
            log.error("Không tìm thấy dấu '/' trong chuỗi.");
        }
        return result;
    }

    public static String extractStringBetweenSlashes(String str) {
        String regex = "/([^/]+)/(\\d+)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getAtView(String requestUri) {
        log.info("Requestt|" + requestUri);
        String atView;
        if (!requestUri.matches(".*\\d.*"))
            atView = Helper.getMediaUri(requestUri);
        else
            atView = Helper.extractStringBetweenSlashes(requestUri);
        log.info("atView|" + atView);
        return atView;
    }

    public static String getDefault30Day() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate thirtyDaysAgo = yesterday.minusDays(29);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return thirtyDaysAgo.format(formatter) + " - " + yesterday.format(formatter);
    }

    public static long calculateDaysBetween(String firstDateString, String secondDateString) {
        LocalDate firstDate = LocalDate.parse(firstDateString, DateTimeFormatter.ISO_DATE);
        LocalDate secondDate = LocalDate.parse(secondDateString, DateTimeFormatter.ISO_DATE);
        return ChronoUnit.DAYS.between(firstDate, secondDate) + 1;
    }

    public static List<String> generateDateArray(String firstDate, String secondDate) {
        List<String> dateArray = new ArrayList<>();

        LocalDate startDate = LocalDate.parse(firstDate);
        LocalDate endDate = LocalDate.parse(secondDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (!startDate.isAfter(endDate)) {
            dateArray.add(startDate.format(formatter));
            startDate = startDate.plusDays(1);
        }

        return dateArray;
    }

    public static List<String> generateMonthArray(String firstMonth, String secondMonth) {
        List<String> monthArray = new ArrayList<>();

        LocalDate startMonth = LocalDate.parse(firstMonth + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endMonth = LocalDate.parse(secondMonth + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        while (!startMonth.isAfter(endMonth)) {
            monthArray.add(startMonth.format(formatter));
            startMonth = startMonth.plusMonths(1);
        }

        return monthArray;
    }

    public static String processAtView(String requestUri) {
        log.info("Requestt|" + requestUri);
        String atView;
        if (!requestUri.matches(".*\\d.*"))
            atView = Helper.getMediaUri(requestUri);
        else
            atView = Helper.extractStringBetweenSlashes(requestUri);
        log.info("atView|" + atView);
        return atView;
    }

    public static Date convertDate(String input) {
        if(input == null) return null;

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm")                 // bắt buộc: yyyy-MM-dd'T'HH:mm
                .optionalStart()
                .appendPattern(":ss")                                // tùy chọn: giây
                .optionalEnd()
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)    // nếu không có giây, gán là 0
                .toFormatter();

        LocalDateTime dateTime = LocalDateTime.parse(input, formatter);
        return java.sql.Timestamp.valueOf(dateTime);
    }

    public static Date convertDateV2(String input) {
        if(input == null) return null;

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")                 // bắt buộc: yyyy-MM-dd HH:mm:ss
                .optionalStart()
                .appendPattern(":ss")                                // tùy chọn: giây
                .optionalEnd()
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)    // nếu không có giây, gán là 0
                .toFormatter();

        LocalDateTime dateTime = LocalDateTime.parse(input, formatter);
        return java.sql.Timestamp.valueOf(dateTime);
    }

    public static JsonArray covertListToArr(List<String> list) {
        JsonArray result = new JsonArray();
        if(list != null) for (String s : list) result.add(s);
        return result;
    }

    public static Date standardDateV2(String input) {
        // Kiểm tra xem chuỗi có phần giây không
        if (input.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
            // Nếu thiếu giây, thêm ":00" vào cuối chuỗi
            input += ":00";
        }

        // Định dạng với giây đầy đủ
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try {
            LocalDateTime dateTime = LocalDateTime.parse(input, formatter);
            Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
            return Date.from(instant);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Chuỗi thời gian không hợp lệ: " + input);
        }
    }

    public static String[] reportDateV3(String input) {
        String[] parts = new String[2];
        try {
            if (input == null || input.equals("")) {
                parts[0] = null;
                parts[1] = null;
            } else {
                parts = input.split("\\ - ");
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return parts;
    }

    public static void deleteFile(String filePath) {
        try {
            if(filePath == null || filePath.isEmpty()){
                log.error("ERROR|" + filePath + " is null or empty");
                return;
            }

            File file = new File(filePath);

            if (file.exists()) {
                if (file.delete())
                    log.info("Tệp đã được xóa thành công.");
                else
                    log.info("Không thể xóa tệp.");
            } else
                log.info("Tệp không tồn tại.");
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty())
            return false;

        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
