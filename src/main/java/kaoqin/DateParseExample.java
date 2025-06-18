package kaoqin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;

public class DateParseExample {
    public static void main(String[] args) {
        String[] testDates = {"2023/10/9", "2023/1/9", "2023/10/19"};

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendValue(ChronoField.YEAR, 4)
                .appendLiteral('/')
                .appendValue(ChronoField.MONTH_OF_YEAR) // 不要求两位
                .appendLiteral('/')
                .appendValue(ChronoField.DAY_OF_MONTH) // 不要求两位
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT); // 严格模式

        for (String dateStr : testDates) {
            LocalDate date = LocalDate.parse(dateStr, formatter);
            System.out.println("Parsed: " + date);
        }
        float a = 4f;
        System.out.println(a);
        String s = String.valueOf(a);
        System.out.println(s);
    }
}
