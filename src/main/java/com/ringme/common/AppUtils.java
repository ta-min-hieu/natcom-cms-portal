package com.ringme.common;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class AppUtils {
    public static Optional<String> goBack(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
    }

    public static Optional<String> goBackWithError(HttpServletRequest request, RedirectAttributes redirectAttributes, String formName, BindingResult bindingResult, Object form, String msgErr) {
        if (msgErr == null) msgErr = "Validation error! Please try again.";
        redirectAttributes.addFlashAttribute(formName, form);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + formName, bindingResult);
        redirectAttributes.addFlashAttribute("error", msgErr);
        return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
    }

    public static Optional<String> goBackWithErrorV2(HttpServletRequest request, RedirectAttributes redirectAttributes, ModelMap model, String msgErr) {
        if (msgErr == null) msgErr = "Validation error! Please try again.";
        for (Map.Entry<String, Object> item : model.entrySet()) {
            redirectAttributes.addFlashAttribute(item.getKey(), item.getValue());
        }
        redirectAttributes.addFlashAttribute("error", msgErr);
        return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
    }

    public static Optional<Object> getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return Optional.ofNullable(field.get(object));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return Optional.empty();
        }
    }

    public static boolean containsVietnameseCharacters(String input) {
        // Regular expression pattern to match Vietnamese characters
        String vietnamesePattern = "[\\p{InCombiningDiacriticalMarks}àáảãạâầấẩẫậăằắẳẵặèéẻẽẹêềếểễệìíỉĩịòóỏõọôồốổỗộơờớởỡợùúủũụưừứửữựỳýỷỹỵđĐ]";
        Pattern pattern = Pattern.compile(vietnamesePattern);
        return pattern.matcher(input).find();
    }

    public static String removeVietnameseAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    public static String removeSpecialCharacters(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
    }

    public static String processTextToAZ09(String input) {
        String noAccents = removeVietnameseAccents(input);
        return removeSpecialCharacters(noAccents);
    }

    public static String extractPath(String url) {
        String path = null;
        if (url.contains("/ringbacktones")) {
            String[] parts = url.split("/ringbacktones", 2);
            if (parts.length > 1) {
                path = "ringbacktones" + parts[1];
            }
        } else if (url.contains("/images")) {
            String[] parts = url.split("/images", 2);
            if (parts.length > 1) {
                path = "images" + parts[1];
            }
        } else if (url.contains("/singer")) {
            String[] parts = url.split("/singer", 2);
            if (parts.length > 1) {
                path = "singer" + parts[1];
            }
        }
        return path;
    }

    public static String capitalizeAndRemoveUnderscore(String input) {
        String[] words = input.split("_");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return result.toString().trim();
    }

    public static String[] getWeekRange(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Thiết lập thời gian cho ngày đầu tuần
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfWeek = calendar.getTime();

        // Thiết lập thời gian cho ngày cuối tuần
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endOfWeek = calendar.getTime();

        // Trả về chuỗi kết quả đã định dạng
        return new String[]{dateFormat.format(startOfWeek), dateFormat.format(endOfWeek)};
    }

    public static String[] getMonthRange(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Thiết lập ngày đầu tháng
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfMonth = calendar.getTime();

        // Thiết lập ngày cuối tháng
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endOfMonth = calendar.getTime();

        return new String[]{dateFormat.format(startOfMonth), dateFormat.format(endOfMonth)};
    }

    public static String[] delegateDateRange(String delegate, Date date) {
        if (delegate == null) return null;
        if (delegate.equals("week"))
            return getWeekRange(date);
        else if (delegate.equals("month"))
            return getMonthRange(date);
        else
            return null;
    }

    public static String truncateString(String value, int length) {
        if (value != null && value.length() > length) {
            return "(Truncated) " + value.substring(0, length) + "...";
        }
        return value;
    }

    public static void main(String[] args) {
        Date now = new Date();
        String[] weekRange = getMonthRange(now);

        System.out.println("Ngày đầu tuần: " + weekRange[0]);
        System.out.println("Ngày cuối tuần: " + weekRange[1]);
    }
}

