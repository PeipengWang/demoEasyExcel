//package excelUtils;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//
//import java.io.*;
//
///**
// * 交换两列数据
// */
//public class ExcelColumnSwapper {
//
//    public static void main(String[] args) {
//        // 输入文件路径
//        String inputPath = "E:\\code\\demoEasyExcel\\generated_excel2.xlsx";
//        // 输出文件路径
//        String outputPath = "E:\\code\\demoEasyExcel\\generated_excel4.xlsx";
//        // 工作表名称
//        String sheetName = "Sheet1";
//        // 要交换的两列（列号从0开始）
//        int col1Index = 1; // 示例：第2列
//        int col2Index = 2; // 示例：第3列
//        // 输出文件路径
//
//        try {
//            swapColumns(inputPath, sheetName, col1Index, col2Index, outputPath);
//            System.out.println("列交换成功！");
//        } catch (Exception e) {
//            System.err.println("列交换失败: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 交换Excel文件中指定工作表的两列
//     * @param filePath 输入文件路径
//     * @param sheetName 工作表名称
//     * @param col1Index 第一列的索引（从0开始）
//     * @param col2Index 第二列的索引（从0开始）
//     * @param outputPath 输出文件路径
//     * @throws IOException 如果文件操作出错
//     */
//    public static void swapColumns(String filePath, String sheetName, int col1Index, int col2Index, String outputPath) throws IOException {
//        try (InputStream is = new FileInputStream(filePath);
//             Workbook workbook = createWorkbook(is, filePath)) {
//
//            // 获取指定工作表
//            Sheet sheet = workbook.getSheet(sheetName);
//            if (sheet == null) {
//                throw new IllegalArgumentException("找不到工作表: " + sheetName);
//            }
//
//            // 获取最大行数
//            int lastRowNum = sheet.getLastRowNum();
//
//            // 遍历每一行，交换指定列的单元格
//            for (int i = 0; i <= lastRowNum; i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) {
//                    row = sheet.createRow(i);
//                }
//
//                // 获取两个单元格
//                Cell cell1 = row.getCell(col1Index);
//                Cell cell2 = row.getCell(col2Index);
//
//                // 创建临时单元格
//                Cell tempCell = row.createCell(row.getLastCellNum() + 1);
//
//                // 复制第一个单元格到临时单元格
//                if (cell1 != null) {
//                    copyCell(cell1, tempCell);
//                } else {
//                    tempCell.setCellType(CellType.BLANK);
//                }
//
//                // 复制第二个单元格到第一个单元格
//                if (cell2 != null) {
//                    copyCell(cell2, cell1);
//                } else {
//                    if (cell1 != null) {
//                        cell1.setCellType(CellType.BLANK);
//                    }
//                }
//
//                // 复制临时单元格到第二个单元格
//                copyCell(tempCell, cell2);
//
//                // 删除临时单元格
//                row.removeCell(tempCell);
//            }
//
//            // 交换列宽
//            int width1 = sheet.getColumnWidth(col1Index);
//            int width2 = sheet.getColumnWidth(col2Index);
//            sheet.setColumnWidth(col1Index, width2);
//            sheet.setColumnWidth(col2Index, width1);
//
//            // 保存修改后的工作簿
//            try (OutputStream os = new FileOutputStream(outputPath)) {
//                workbook.write(os);
//            }
//        }
//    }
//
//    /**
//     * 根据文件扩展名创建适当的Workbook实例
//     */
//    private static Workbook createWorkbook(InputStream is, String filePath) throws IOException {
//        if (filePath.endsWith(".xlsx")) {
//            return new XSSFWorkbook(is);
//        } else if (filePath.endsWith(".xls")) {
//            return new HSSFWorkbook(is);
//        } else {
//            throw new IllegalArgumentException("不支持的文件格式: " + filePath);
//        }
//    }
//
//    /**
//     * 复制单元格内容和样式
//     */
//    private static void copyCell(Cell sourceCell, Cell targetCell) {
//        // 复制单元格样式
//        targetCell.setCellStyle(sourceCell.getCellStyle());
//
//        // 复制单元格值
//        switch (sourceCell.getCellType()) {
//            case STRING:
//                targetCell.setCellValue(sourceCell.getStringCellValue());
//                break;
//            case NUMERIC:
//                targetCell.setCellValue(sourceCell.getNumericCellValue());
//                break;
//            case BOOLEAN:
//                targetCell.setCellValue(sourceCell.getBooleanCellValue());
//                break;
//            case FORMULA:
//                targetCell.setCellFormula(sourceCell.getCellFormula());
//                break;
//            case BLANK:
//                targetCell.setCellType(CellType.BLANK);
//                break;
//            case ERROR:
//                targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
//                break;
//            default:
//                break;
//        }
//    }
//}