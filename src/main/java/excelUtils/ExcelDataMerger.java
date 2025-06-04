package excelUtils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 合并表格数据为字符串
 */
public class ExcelDataMerger {

    public static void main(String[] args) {
        // 输入参数
        String formatTemplate = "${1}:${2}"; // 格式模板
        String delimiter = ";";              // 分隔符
        String filePath = "E:\\code\\demoEasyExcel\\generated_excel2.xlsx";
        
        try {
            String mergedResult = mergeExcelData(filePath, formatTemplate, delimiter);
            System.out.println("合并结果: " + mergedResult);
        } catch (Exception e) {
            System.err.println("合并失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 按指定格式合并Excel数据
     */
    public static String mergeExcelData(String filePath, String formatTemplate, String delimiter) throws IOException {
        StringBuilder resultBuilder = new StringBuilder();
        
        try (InputStream is = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(is)) {
            
            // 获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            
            // 编译格式模板中的占位符正则表达式
            Pattern pattern = Pattern.compile("\\$\\{(\\d+)}");
            
            // 遍历每一行（从第1行开始，跳过表头）
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;
                
                // 处理当前行，替换格式模板中的占位符
                String rowResult = processRow(row, formatTemplate, pattern);
                
                // 添加到结果中，使用分隔符分隔
                if (resultBuilder.length() > 0) {
                    resultBuilder.append(delimiter);
                }
                resultBuilder.append(rowResult);
            }
        }
        
        return resultBuilder.toString();
    }

    /**
     * 处理单行数据，替换格式模板中的占位符
     */
    private static String processRow(Row row, String formatTemplate, Pattern pattern) {
        String result = formatTemplate;
        
        // 查找并替换所有占位符
        Matcher matcher = pattern.matcher(result);
        while (matcher.find()) {
            // 获取占位符中的列索引（注意Excel列索引从0开始）
            int columnIndex = Integer.parseInt(matcher.group(1)) - 1;
            
            // 获取对应单元格的值
            String cellValue = getCellValueAsString(row.getCell(columnIndex));
            
            // 替换占位符
            result = result.replace(matcher.group(0), cellValue);
        }
        
        return result;
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