package kaoqin;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class WorkDayJiNan {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("E:\\code\\WorkManager\\2025年4月-5月\\2025年4月-5月\\原始数据\\34室考勤数据-5月份.xlsx");
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);


        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            // 读取姓名
            String name = row.getCell(0).getStringCellValue().trim();

            // 读取上班时间和日期
            String onTimeStr = row.getCell(6).getStringCellValue().trim(); // 上班时间列
            String hourStr = row.getCell(9).getStringCellValue().trim();  // 考勤日期列
            String dateStr = row.getCell(17).getStringCellValue().trim(); // 上班时间列
            String mornTime = geCarTime(onTimeStr, dateStr);
            String afterNoon = geCarTime(hourStr, dateStr);
        }

        workbook.close();
        fis.close();

    }
    public static String geCarTime(String hourStr, String dateStr) {
        if (hourStr != null && hourStr.length() >= 8) {
            return dateStr + " "+hourStr;
        }
        return "";
    }
}
