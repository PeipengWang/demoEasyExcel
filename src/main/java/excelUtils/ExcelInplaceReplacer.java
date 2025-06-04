package excelUtils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/**
 * 替换某列的数据
 */
public class ExcelInplaceReplacer {

    public static void main(String[] args) {
        // 示例数据映射
        Map<String, String> replacementMap = new HashMap<>();
        replacementMap.put("Row1-Col1", "Row1-Col1:1;Row1-Col1:0;Row1-Col1:2;Row1-Col1:3;Row1-Col1:4;Row1-Col1:5;Row1-Col1:6;Row1-Col1:7;Row1-Col1:8;Row1-Col1:9;Row1-Col1:10;Row1-Col1:11;Row1-Col1:12;Row1-Col1:13;Row1-Col1:14;Row1-Col1:15;Row1-Col1:16;Row1-Col1:17;Row1-Col1:18;Row1-Col1:19;Row1-Col1:20;Row1-Col1:21");
        replacementMap.put("Row1-Col2", "value2");
        replacementMap.put("key3", "value3");
        
        // 输入参数
        String filePath = "E:\\code\\demoEasyExcel\\generated_excel2.xlsx";
        int columnIndex = 0; // 要替换数据的列索引（从0开始，例如0表示第一列）
        
        try {
            replaceCellValuesInPlace(filePath, columnIndex, replacementMap);
            System.out.println("数据替换成功！");
        } catch (Exception e) {
            System.err.println("替换失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 在原文件中根据Map替换Excel表格中指定列的数据
     */
    public static void replaceCellValuesInPlace(String filePath, int columnIndex, 
                                               Map<String, String> replacementMap) throws IOException {
        // 备份原文件
        backupFile(filePath);
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = createWorkbook(fis, filePath)) {
            
            // 获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            
            // 遍历每一行（从第1行开始，跳过表头）
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;
                
                // 获取指定列的单元格
                Cell cell = row.getCell(columnIndex);
                if (cell == null) continue;
                
                // 获取单元格的值
                String cellValue = getCellValueAsString(cell);
                
                // 如果Map中存在该key，则替换单元格的值
                if (replacementMap.containsKey(cellValue)) {
                    cell.setCellValue(replacementMap.get(cellValue));
                }
            }
            
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
     * 获取单元格的值并转换为字符串
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            default:
                return "";
        }
    }
}  