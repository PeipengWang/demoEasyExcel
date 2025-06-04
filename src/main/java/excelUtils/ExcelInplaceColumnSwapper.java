package excelUtils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * 源文件直接交换两列
 */
public class ExcelInplaceColumnSwapper {

    public static void main(String[] args) {
        // 源文件路径（会被直接修改）
        String filePath = "E:\\code\\demoEasyExcel\\generated_excel2.xlsx";
        // 要交换的两列（列号从0开始）
        int col1Index = 1; // 示例：第2列
        int col2Index = 2; // 示例：第3列
        
        try {
            // 备份原文件
            backupFile(filePath);
            // 交换列
            swapColumnsInPlace(filePath, col1Index, col2Index);
            System.out.println("列交换成功！");
        } catch (Exception e) {
            System.err.println("列交换失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 在源文件中交换指定的两列
     */
    public static void swapColumnsInPlace(String filePath, int col1Index, int col2Index) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = createWorkbook(fis, filePath)) {
            
            // 获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            
            // 获取最大行数
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
            
            // 保存修改后的工作簿到原文件
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * 备份文件
     */
    private static void backupFile(String filePath) throws IOException {
        File originalFile = new File(filePath);
        File backupFile = new File(filePath + ".bak");
        
        // 检查备份文件是否存在，如果存在则删除
        if (backupFile.exists()) {
            backupFile.delete();
        }
        
        // 复制原文件到备份文件
        Files.copy(originalFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
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

    /**
     * 复制单元格内容和样式
     */
    private static void copyCell(Cell sourceCell, Cell targetCell) {
        // 复制单元格样式
        targetCell.setCellStyle(sourceCell.getCellStyle());

        // 复制单元格值
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
}    