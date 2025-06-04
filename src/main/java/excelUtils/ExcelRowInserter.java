package excelUtils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * 插入一行数据
 */
public class ExcelRowInserter {

    public static void main(String[] args) {
        // 输入参数
        String filePath = "path/to/your/excel/file.xlsx"; // Excel文件路径
        int insertRowIndex = 2; // 插入位置（从0开始，例如2表示在第3行前插入）
        
        // 要插入的数据（列数应与表格列数匹配）
        List<String> rowData = Arrays.asList("New Data 1", "New Data 2", "New Data 3");
        
        try {
            insertRow(filePath, insertRowIndex, rowData);
            System.out.println("行插入成功！");
        } catch (Exception e) {
            System.err.println("插入失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 在指定位置插入一行数据
     */
    public static void insertRow(String filePath, int insertRowIndex, List<String> rowData) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = createWorkbook(fis, filePath)) {
            
            // 获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            
            // 下移后续行
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum >= insertRowIndex) {
                sheet.shiftRows(insertRowIndex, lastRowNum, 1, true, false);
            }
            
            // 创建新行
            Row newRow = sheet.createRow(insertRowIndex);
            
            // 设置行高（使用上一行的行高或默认值）
            if (insertRowIndex > 0) {
                Row previousRow = sheet.getRow(insertRowIndex - 1);
                if (previousRow != null) {
                    newRow.setHeight(previousRow.getHeight());
                }
            }
            
            // 填充数据
            for (int i = 0; i < rowData.size(); i++) {
                Cell cell = newRow.createCell(i);
                cell.setCellValue(rowData.get(i));
                
                // 尝试复制上一行的样式
                if (insertRowIndex > 0) {
                    Row previousRow = sheet.getRow(insertRowIndex - 1);
                    if (previousRow != null) {
                        Cell previousCell = previousRow.getCell(i);
                        if (previousCell != null) {
                            cell.setCellStyle(previousCell.getCellStyle());
                        }
                    }
                }
            }
            
            // 保存修改后的工作簿
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * 根据文件扩展名创建适当的Workbook实例
     */
    private static Workbook createWorkbook(InputStream is, String filePath) throws IOException {
        if (filePath.endsWith(".xlsx")) {
            return new XSSFWorkbook(is);
        } else if (filePath.endsWith(".xls")) {
            return new HSSFWorkbook(is);
        } else {
            throw new IllegalArgumentException("不支持的文件格式: " + filePath);
        }
    }
}    