package kaoqin;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelColorExample {
    public static void main(String[] args) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("示例表");

        // 创建行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("红色单元格");

        // 创建红色背景样式
        HSSFCellStyle redStyle = workbook.createCellStyle();
        redStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        redStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 应用样式
        cell.setCellStyle(redStyle);
        HSSFCell cell1 = row.createCell(1);
        cell1.setCellValue("白色单元格");
        // 写入文件
        try (FileOutputStream out = new FileOutputStream("红色单元格示例.xls")) {
            workbook.write(out);
            workbook.close();
        }

        System.out.println("Excel文件已生成！");
    }
}
