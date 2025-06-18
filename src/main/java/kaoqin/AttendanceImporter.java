package kaoqin;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AttendanceImporter {
    public static void main(String[] args) throws Exception {
        String filename = "D:\\Java\\apache-tomcat-9.0.50\\bin\\upload\\2eaa0b75-08a1-408b-9c13-2028c12dd925_1_(4月)员工刷卡记录表.xls";
        FileInputStream fis = new FileInputStream(filename);
        Workbook workbook;
        if (filename.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(fis);  // 处理 .xlsx 文件
        } else if (filename.endsWith(".xls")) {
            workbook = new HSSFWorkbook(fis);  // 处理 .xls 文件
        } else {
            throw new IllegalArgumentException("Unsupported file type.");
        }
        getRecordsByXiAn(workbook);

    }
    public static List<AttendanceRecord> getRecordsByXiAn(Workbook workbook) throws IOException {
        Sheet sheet = workbook.getSheetAt(0);
        List<AttendanceRecord> result = new ArrayList<>();
        String startDateStr = null, endDateStr = null;
        List<String> dateList = new ArrayList<>();
        int rowNum = sheet.getLastRowNum();
        String username = null, cardid = null;
        for (int i = 0; i < 4; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            for (Cell cell : row) {
                String text = cell.toString().trim();
                // 1. 获取考勤时间段
                if (text.startsWith("考勤日期：")) {
                    String dateRange = text.replace("考勤日期：", "").replace("～", "~");
                    String[] parts = dateRange.split("~");
                    startDateStr = parts[0].trim();
                    endDateStr = parts[1].trim();
                    dateList = getDateRange(startDateStr, endDateStr);
                    break;
                }
            }
        }
        for (int i = 4; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            boolean isNewUserRow = false;
            String tempUsername = null;
            String tempCardid = null;

            for (Cell cell : row) {
                String text = cell.toString().trim();

                if (text.startsWith("姓名：")) {
                    Cell nameCell = row.getCell(cell.getColumnIndex() + 1);
                    if (nameCell != null) {
                        tempUsername = nameCell.toString().trim();
                    }
                    isNewUserRow = true;
                }

                if (text.startsWith("工号：")) {
                    Cell idCell = row.getCell(cell.getColumnIndex() + 1);
                    if (idCell != null) {
                        tempCardid = idCell.toString().trim();
                    }
                    isNewUserRow = true;
                }
            }
            // 如果是新用户行，更新用户信息
            if (isNewUserRow) {
                username = tempUsername;
                cardid = tempCardid;
                continue; // 此行不包含打卡数据，跳过
            }

            // 如果当前有用户信息，就解析这一行的打卡时间（可能是多行）
            if (username != null && cardid != null) {
                for (int j = 0; j < dateList.size(); j++) {
                    Cell cell = row.getCell(j+1); // 假设打卡时间从第2列开始
                    if (cell != null && !cell.toString().trim().isEmpty() && cell.toString().contains(":")) {
                        String[] timeRecords = cell.toString().split("\\s+");
                        for (String timeRecord : timeRecords) {
                            String fullDateTime = dateList.get(j) + " " + timeRecord;
                            AttendanceRecord record = new AttendanceRecord();
                            record.setUsername(username);
                            record.setCardid(cardid);
                            record.setCardTime(fullDateTime);
                            record.setLocation("西安分组"); // 可根据实际情况动态获取
                            result.add(record);
                        }
                    }
                }
            }
        }
        // 打印验证或写入数据库
        for (AttendanceRecord r : result) {
            System.out.println(r);
            // insertToMySQL(r); // 可替换为数据库操作
        }
        if(workbook != null){
            workbook.close();
        }
        return result;
    }

    // 工具方法：生成起止日期范围列表
    public static List<String> getDateRange(String start, String end) {
        List<String> dates = new ArrayList<>();
        LocalDate s = LocalDate.parse(start, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        LocalDate e = LocalDate.parse(end, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        while (!s.isAfter(e)) {
            dates.add(s.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            s = s.plusDays(1);
        }
        return dates;
    }
}
