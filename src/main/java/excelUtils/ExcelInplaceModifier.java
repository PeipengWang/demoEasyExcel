package excelUtils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;

/**
 * 源文件交换两行
 */
public class ExcelInplaceModifier {

    public static void main(String[] args) {
        // 输入文件路径（会被直接修改）
        String filePath = "E:\\code\\demoEasyExcel\\generated_excel2.xlsx";
        // 工作表名称
        String sheetName = "Sheet1";
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = createWorkbook(fis, filePath)) {
            
            // 交换第3行和第6行（行号从0开始）
            swapRows(workbook, sheetName, 1, 2);
            
            // 直接覆盖原文件
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            
            System.out.println("原地修改成功！");
        } catch (Exception e) {
            System.err.println("修改失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 交换指定工作表中的两行
     */
    public static void swapRows(Workbook workbook, String sheetName, int row1Index, int row2Index) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("找不到工作表: " + sheetName);
        }

        int lastRowNum = sheet.getLastRowNum();
        if (row1Index < 0 || row1Index > lastRowNum || row2Index < 0 || row2Index > lastRowNum) {
            throw new IllegalArgumentException("行索引超出范围");
        }

        Row row1 = sheet.getRow(row1Index);
        Row row2 = sheet.getRow(row2Index);

        if (row1 == null) row1 = sheet.createRow(row1Index);
        if (row2 == null) row2 = sheet.createRow(row2Index);

        // 创建临时行
        Row tempRow = sheet.createRow(lastRowNum + 1);
        copyRow(row1, tempRow);
        copyRow(row2, row1);
        copyRow(tempRow, row2);
        
        // 删除临时行
        sheet.removeRow(tempRow);
    }

    /**
     * 交换指定工作表中的两列
     */
    public static void swapColumns(Workbook workbook, String sheetName, int col1Index, int col2Index) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("找不到工作表: " + sheetName);
        }

        int lastRowNum = sheet.getLastRowNum();
        
        // 遍历每一行，交换指定列的单元格
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            
            Cell cell1 = row.getCell(col1Index);
            Cell cell2 = row.getCell(col2Index);
            
            // 创建临时单元格
            Cell tempCell = row.createCell(row.getLastCellNum() + 1);
            
            if (cell1 != null) copyCell(cell1, tempCell);
            else tempCell.setCellType(CellType.BLANK);
            
            if (cell2 != null) copyCell(cell2, cell1);
            else if (cell1 != null) cell1.setCellType(CellType.BLANK);
            
            copyCell(tempCell, cell2);
            row.removeCell(tempCell);
        }
        
        // 交换列宽
        int width1 = sheet.getColumnWidth(col1Index);
        int width2 = sheet.getColumnWidth(col2Index);
        sheet.setColumnWidth(col1Index, width2);
        sheet.setColumnWidth(col2Index, width1);
    }

    // 辅助方法：复制行
    private static void copyRow(Row sourceRow, Row targetRow) {
        targetRow.setHeight(sourceRow.getHeight());
        targetRow.setRowStyle(sourceRow.getRowStyle());
        
        for (int i = 0; i <= sourceRow.getLastCellNum(); i++) {
            Cell sourceCell = sourceRow.getCell(i);
            if (sourceCell != null) {
                Cell targetCell = targetRow.getCell(i);
                if (targetCell == null) {
                    targetCell = targetRow.createCell(i);
                }
                copyCell(sourceCell, targetCell);
            }
        }
    }

    // 辅助方法：复制单元格
    private static void copyCell(Cell sourceCell, Cell targetCell) {
        targetCell.setCellStyle(sourceCell.getCellStyle());
        
        switch (sourceCell.getCellType()) {
            case STRING:
                targetCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                targetCell.setCellValue(sourceCell.getNumericCellValue());
                break;
            case BOOLEAN:
                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case FORMULA:
                targetCell.setCellFormula(sourceCell.getCellFormula());
                break;
            case BLANK:
                targetCell.setCellType(CellType.BLANK);
                break;
            case ERROR:
                targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
                break;
            default:
                break;
        }
    }

    // 创建工作簿
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