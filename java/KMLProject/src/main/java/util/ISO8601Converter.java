package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ISO8601Converter {
    public static String convertToISO8601(String dateStr) {
        if (dateStr == null) return null;

        try {
            String[] parts = dateStr.split(" ");
            int day = Integer.parseInt(parts[0].substring(0, 2));
            int hour = Integer.parseInt(parts[0].substring(2, 4));
            int minute = Integer.parseInt(parts[0].substring(4, 6));
            String monthStr = parts[2];

            Map<String, Integer> months = new HashMap<>();
            months.put("JAN", 1); months.put("FEB", 2); months.put("MAR", 3);
            months.put("APR", 4); months.put("MAY", 5); months.put("JUN", 6);
            months.put("JUL", 7); months.put("AUG", 8); months.put("SEP", 9);
            months.put("OCT", 10); months.put("NOV", 11); months.put("DEC", 12);

            int month = months.getOrDefault(monthStr, 0);
            int year = LocalDateTime.now().getYear();

            LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute);
            return dateTime.format(DateTimeFormatter.ISO_DATE_TIME) + "Z";
        } catch (Exception e) {
            return null;
        }
    }
}