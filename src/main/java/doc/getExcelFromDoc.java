package doc;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.util.List;

public class getExcelFromDoc {
    public static void main(String[] args) throws IOException {
        // 1. 打开 Word 文件
        FileInputStream fis = new FileInputStream("E:\\code\\demoEasyExcel\\王培鹏_2025年6月半月报（上）.docx");
        XWPFDocument document = new XWPFDocument(fis);

        // 2. 获取所有表格
        List<XWPFTable> tables = document.getTables();

        int tableIndex = 1;
        for (XWPFTable table : tables) {
            // 3. 每个表格导出到单独 CSV 文件
            String fileName = "table_" + tableIndex + ".csv";
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(fileName))) {
                for (XWPFTableRow row : table.getRows()) {
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (int i = 0; i < cells.size(); i++) {
                        writer.print(cells.get(i).getText().replaceAll(",", "，")); // 替换逗号避免 CSV 错误
                        if (i < cells.size() - 1) {
                            writer.print(",");
                        }
                    }
                    writer.println();
                }
                System.out.println("表格已导出：" + fileName);
            }
            tableIndex++;
        }

        document.close();
        fis.close();
    }
}
