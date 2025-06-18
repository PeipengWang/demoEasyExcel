package kaoqin;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorkdayGenerator {
    
    // 2025年中国法定节假日
    private static final Map<String, String> HOLIDAYS = new HashMap<>();
    static {
        // 元旦
        HOLIDAYS.put("2025-01-01", "元旦");
        
        // 春节
        HOLIDAYS.put("2025-01-28", "春节");
        HOLIDAYS.put("2025-01-29", "春节");
        HOLIDAYS.put("2025-01-30", "春节");
        HOLIDAYS.put("2025-01-31", "春节");
        HOLIDAYS.put("2025-02-01", "春节");
        HOLIDAYS.put("2025-02-02", "春节");
        HOLIDAYS.put("2025-02-03", "春节");
        
        // 清明节
        HOLIDAYS.put("2025-04-04", "清明节");
        HOLIDAYS.put("2025-04-05", "清明节");
        HOLIDAYS.put("2025-04-06", "清明节");
        
        // 劳动节
        HOLIDAYS.put("2025-05-01", "劳动节");
        HOLIDAYS.put("2025-05-02", "劳动节");
        HOLIDAYS.put("2025-05-03", "劳动节");
        HOLIDAYS.put("2025-05-04", "劳动节");
        HOLIDAYS.put("2025-05-05", "劳动节");
        
        // 端午节
        HOLIDAYS.put("2025-05-31", "端午节");
        HOLIDAYS.put("2025-06-01", "端午节");
        HOLIDAYS.put("2025-06-02", "端午节");
        
        // 中秋节
        HOLIDAYS.put("2025-09-21", "中秋节");
        HOLIDAYS.put("2025-09-22", "中秋节");
        HOLIDAYS.put("2025-09-23", "中秋节");
        
        // 国庆节
        HOLIDAYS.put("2025-10-01", "国庆节");
        HOLIDAYS.put("2025-10-02", "国庆节");
        HOLIDAYS.put("2025-10-03", "国庆节");
        HOLIDAYS.put("2025-10-04", "国庆节");
        HOLIDAYS.put("2025-10-05", "国庆节");
        HOLIDAYS.put("2025-10-06", "国庆节");
        HOLIDAYS.put("2025-10-07", "国庆节");
    }
    
    // 2025年调休工作日（周末上班）
    private static final Set<String> WORK_WEEKENDS = new HashSet<>();
    static {
        WORK_WEEKENDS.add("2025-01-25"); // 春节调休
        WORK_WEEKENDS.add("2025-01-26"); // 春节调休
        WORK_WEEKENDS.add("2025-04-27"); // 劳动节调休
        WORK_WEEKENDS.add("2025-05-10"); // 端午节调休
        WORK_WEEKENDS.add("2025-09-20"); // 中秋节调休
        WORK_WEEKENDS.add("2025-09-27"); // 国庆节调休
        WORK_WEEKENDS.add("2025-10-11"); // 国庆节调休
    }
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static void main(String[] args) {
        String userId = "1502";
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        
        StringBuilder sqlBuilder = new StringBuilder();
        
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            String dateStr = currentDate.format(DATE_FORMATTER);
            String dayType;
            
            if (HOLIDAYS.containsKey(dateStr)) {
                // 法定节假日
                dayType = HOLIDAYS.get(dateStr);
            } else if (WORK_WEEKENDS.contains(dateStr)) {
                // 调休工作日（周末上班）
                dayType = "工作日";
            } else {
                // 判断是否是周末
                int dayOfWeek = currentDate.getDayOfWeek().getValue(); // 1-7 (1是周一，7是周日)
                if (dayOfWeek == 6 || dayOfWeek == 7) {
                    dayType = "休息日";
                } else {
                    dayType = "工作日";
                }
            }
            
            String sql = String.format("INSERT INTO worktime (dateDay,status) VALUES ( '%s', '%s');",
                                       dateStr, dayType);
            sqlBuilder.append(sql).append("\n");
            
            currentDate = currentDate.plusDays(1);
        }
        
        // 输出部分示例
        String[] allSqls = sqlBuilder.toString().split("\n");
        System.out.println("示例SQL (前10条):");
        for (int i = 0; i < 10 && i < allSqls.length; i++) {
            System.out.println(allSqls[i]);
        }
        
        System.out.println("\n...\n");
        
        System.out.println("示例SQL (最后10条):");
        for (int i = Math.max(0, allSqls.length - 10); i < allSqls.length; i++) {
            System.out.println(allSqls[i]);
        }
        
        // 写入文件
        try (FileWriter writer = new FileWriter("worktime_2025.sql")) {
            writer.write(sqlBuilder.toString());
            System.out.println("\n已生成 worktime_2025.sql 文件");
        } catch (IOException e) {
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }
}