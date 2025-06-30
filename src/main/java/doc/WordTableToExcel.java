package doc;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.List;

public class WordTableToExcel {
    public static void main(String[] args) throws Exception {
        String inputPath = "E:\\code\\demoEasyExcel\\王培鹏_2025年6月半月报（上）.docx";   // Word 文件路径
        String outputPath = "output.xlsx"; // 导出 Excel 路径
        FileInputStream fis = new FileInputStream(inputPath);
        XWPFDocument document = new XWPFDocument(fis);
        XSSFWorkbook workbook = new XSSFWorkbook();

        List<IBodyElement> bodyElements = document.getBodyElements();
        int tableIndex = 1;
        String currentTableTitle = "表" + tableIndex;
        int elementIndex = 0;

        while (elementIndex < bodyElements.size()) {
            IBodyElement element = bodyElements.get(elementIndex);

            // 检测段落是否是“表n”标题
            if (element instanceof XWPFParagraph) {
                XWPFParagraph para = (XWPFParagraph) element;
                String text = para.getText().trim();
                if (text.matches("表\\d+.*")) {
                    currentTableTitle = text.split("\\s+")[0]; // 提取“表1”作为 sheet 名
                }
            }

            // 检测是否是表格
            if (element instanceof XWPFTable) {
                XWPFTable table = (XWPFTable) element;

                // 创建新 sheet（避免重名）
                String sheetName = currentTableTitle.replaceAll("[\\\\/:*?\"<>|]", "_"); // Excel 名称合法化
                XSSFSheet sheet = workbook.createSheet(sheetName);

                int rowNum = 0;
                for (XWPFTableRow row : table.getRows()) {
                    Row excelRow = sheet.createRow(rowNum++);
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (int col = 0; col < cells.size(); col++) {
                        Cell cell = excelRow.createCell(col);
                        cell.setCellValue(cells.get(col).getText());
                    }
                }

                tableIndex++;
            }

            elementIndex++;
        }

        // 写入 Excel
        FileOutputStream fos = new FileOutputStream(outputPath);
        workbook.write(fos);
        workbook.close();
        document.close();
        fis.close();
        fos.close();
        System.out.println("表格已导出到 Excel：" + outputPath);
    }
}
