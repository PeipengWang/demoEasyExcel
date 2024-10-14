package poi;

import entity.ExcelData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class POIExcelReader {

    public static void main(String[] args) {
        String filePath = "E:\\code\\demoEasyExcel\\generated_excel1.xlsx";
        long l = System.currentTimeMillis();
        List<ExcelData> dataList = readExcel(filePath);
        long l1 = System.currentTimeMillis();

        System.out.println("消耗时间：" + (l1 - l)/1000);
        for (ExcelData data : dataList) {
        }
        long l2 = System.currentTimeMillis();
        System.out.println("打印消耗时间：" + (l2 - l1)/1000);
        System.out.println("总消耗时间：" + (l2 - l)/1000);
    }
    public static List<ExcelData> readExcel(String filePath) {
        List<ExcelData> dataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    // 跳过表头
                    continue;
                }

                ExcelData data = new ExcelData();

                data.setColumn1(getStringCellValue(row.getCell(0)));
                data.setColumn2(getIntegerCellValue(row.getCell(1)));
                data.setColumn3(getDateCellValue(row.getCell(2)));

                dataList.add(data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private static String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : cell.toString();
    }

    private static Integer getIntegerCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        return cell.getCellType() == CellType.NUMERIC ? (int) cell.getNumericCellValue() : null;
    }

    private static Date getDateCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        return cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue() : null;
    }
}