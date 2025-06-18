package kaoqin;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;

import java.time.LocalDate;
import java.util.*;

public class AttendanceExcelToMySQL {

    public static void main(String[] args) throws Exception {
        String excelPath = "E:\\code\\WorkManager\\2025年4月-5月\\2025年4月-5月\\原始数据\\307LAB_打卡时间表_20250416-20250516(1).xlsx";
        String jdbcUrl = "jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=UTC";
        String jdbcUser = "root";
        String jdbcPassword = "your_password";

        List<AttendanceRecord> records = parseExcel(excelPath);
        for (AttendanceRecord record: records){
            System.out.println(record.toString());
        }
        insertIntoDatabase(records, jdbcUrl, jdbcUser, jdbcPassword);

        System.out.println("✅ 导入成功，共导入 " + records.size() + " 条记录");
    }

    static List<AttendanceRecord> parseExcel(String path) throws Exception {
        List<AttendanceRecord> result = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(path))) {
            Sheet sheet = workbook.getSheetAt(0);

            // ===== 1. 提取统计日期范围（从 A1 单元格） =====
            Row titleRow = sheet.getRow(0);
            String titleCell = titleRow.getCell(0).getStringCellValue(); // A1

            String dateRangeStr = titleCell.split("统计日期：")[1].trim();
            String[] parts = dateRangeStr.split("至");
            LocalDate startDate = LocalDate.parse(parts[0].trim());
            LocalDate endDate = LocalDate.parse(parts[1].trim());

            // ===== 2. 构造日期列表 =====
            List<LocalDate> dateList = new ArrayList<>();
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                dateList.add(date);
            }
            // ===== 3. 遍历每一行，从第2行（下标1）开始读取姓名与打卡记录 =====
            for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                String name = row.getCell(0).getStringCellValue();  // 姓名列
                String group = row.getCell(1).getStringCellValue(); // 考勤组列
                for (int j = 0; j < dateList.size(); j++) {
                    LocalDate date = dateList.get(j);
                    Cell cell = row.getCell(j + 6);// 从第3列开始（列索引2）
                    if (cell != null && cell.getCellType() != CellType.BLANK) {
                        String timeRecord = cell.getStringCellValue();
                        if(timeRecord.contains(":")){
                            String[] times = timeRecord.split("\n");
                            for (String time : times){
                                time = time.trim();
                                if(!time.isEmpty())
                                result.add(new AttendanceRecord(name, group, date, time));
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    static void insertIntoDatabase(List<AttendanceRecord> records, String url, String user, String pwd) {
//        try (Connection conn = DriverManager.getConnection(url, user, pwd)) {
//            String sql = "INSERT INTO attendance(name, group_name, date, time_record) VALUES (?, ?, ?, ?)";
//            try (PreparedStatement ps = conn.prepareStatement(sql)) {
//                for (AttendanceRecord r : records) {
//                    ps.setString(1, r.name);
//                    ps.setString(2, r.group);
//                    ps.setDate(3, Date.valueOf(r.date));
//                    ps.setString(4, r.timeRecord);
//                    ps.addBatch();
//                }
//                ps.executeBatch();
//            }
//        }
    }

    static class AttendanceRecord {
        String name;
        String group;
        LocalDate date;
        String timeRecord;

        AttendanceRecord(String name, String group, LocalDate date, String timeRecord) {
            this.name = name;
            this.group = group;
            this.date = date;
            this.timeRecord = timeRecord;
        }
        @Override
        public String toString() {
            return "AttendanceRecord{" +
                    "name='" + name + '\'' +
                    ", group='" + group + '\'' +
                    ", timeRecord='" + timeRecord + '\'' +
                    ", date=" + date +
                    '}';
        }
    }
}
