package excelUtils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;

/**
 * 交换两行数据，生成新表
 */
public class ExcelRowSwapper {

    public static void main(String[] args) {

        // 输入文件路径
        String inputPath = "E:\\code\\demoEasyExcel\\generated_excel2.xlsx";
        // 输出文件路径
        String outputPath = "E:\\code\\demoEasyExcel\\generated_excel3.xlsx";
        // 工作表名称
        String sheetName = "Sheet1";
        // 要交换的两行（行号从0开始）
        int row1Index = 2; // 示例：第3行
        int row2Index = 5; // 示例：第6行

        try {
            swapRows(inputPath, sheetName, row1Index, row2Index, outputPath);
            System.out.println("行交换成功！");
        } catch (Exception e) {
            System.err.println("行交换失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 交换Excel文件中指定工作表的两行
     * @param filePath 输入文件路径
     * @param sheetName 工作表名称
     * @param row1Index 第一行的索引（从0开始）
     * @param row2Index 第二行的索引（从0开始）
     * @param outputPath 输出文件路径
     * @throws IOException 如果文件操作出错
     */
    public static void swapRows(String filePath, String sheetName, int row1Index, int row2Index, String outputPath) throws IOException {
        try (InputStream is = new FileInputStream(filePath);
             Workbook workbook = createWorkbook(is, filePath)) {

            // 获取指定工作表
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("找不到工作表: " + sheetName);
            }

            // 确保行索引有效
            int lastRowNum = sheet.getLastRowNum();
            if (row1Index < 0 || row1Index > lastRowNum || row2Index < 0 || row2Index > lastRowNum) {
                throw new IllegalArgumentException("行索引超出范围: 最大行号为 " + lastRowNum);
            }

            // 获取两行
            Row row1 = sheet.getRow(row1Index);
            Row row2 = sheet.getRow(row2Index);

            // 如果行不存在，创建空行
            if (row1 == null) {
                row1 = sheet.createRow(row1Index);
            }
            if (row2 == null) {
                row2 = sheet.createRow(row2Index);
            }

            // 创建临时行
            Row tempRow = sheet.createRow(lastRowNum + 1);
            copyRow(row1, tempRow);

            // 交换内容
            copyRow(row2, row1);
            copyRow(tempRow, row2);

            // 删除临时行
            sheet.removeRow(tempRow);

            // 保存修改后的工作簿
            try (OutputStream os = new FileOutputStream(outputPath)) {
                workbook.write(os);
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

    /**
     * 复制一行的内容到另一行
     */
    private static void copyRow(Row sourceRow, Row targetRow) {
        // 复制行样式和高度
        targetRow.setHeight(sourceRow.getHeight());
        targetRow.setRowStyle(sourceRow.getRowStyle());

        // 复制单元格
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