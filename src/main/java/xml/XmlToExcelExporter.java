package xml;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XmlToExcelExporter {

    // 表头内容（按你的顺序）
    static final String[] HEADERS = {
        "序号", "分系统", "所属设备", "APID（十六进制）", "源包名称", "参数代号", "参数名称", "类型",
        "通道号（十进制）", "路号(Byte)", "位号(bit)", "单位", "处理精度（小数点后几位）", "源码数据类型",
        "地面处理公式", "遥测解析后正常值范围", "系数a", "系数b", "系数c", "系数d", "系数e", "系数f", "系数g",
        "系数h", "系数i", "系数j", "复杂系数a", "复杂系数b", "复杂系数c", "复杂系数d", "复杂系数e",
        "数据位宽（bit）", "帧频（s）", "是否大端", "备注", "", "系数a"
    };

    public static void main(String[] args) throws IOException {
        // 1. 解析 XML 文件（允许不规范）
        File inputXml = new File("src/main/java/xml/text1.xml"); // 替换为你的 XML 路径
        Document doc = Jsoup.parse(inputXml, "UTF-8", "", Parser.xmlParser());

        // 2. 创建 Excel 工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("遥测参数");

        // 3. 写入表头
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) {
            headerRow.createCell(i).setCellValue(HEADERS[i]);
        }

        // 4. 遍历每个参数节点并写入数据（此处仅示例解析逻辑）
        Elements parameters = doc.select("param"); // 假设每个遥测参数包裹在 <param> 标签中
        int rowNum = 1;

        for (Element param : parameters) {
            Row row = sheet.createRow(rowNum++);
            int col = 0;

            // 示例字段填充（根据实际 XML 中结构修改）
            row.createCell(col++).setCellValue(rowNum - 1); // 序号
            row.createCell(col++).setCellValue("地面分系统"); // 分系统
            row.createCell(col++).setCellValue("卫星");       // 所属设备
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "apid"));       // APID（十六进制）
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "packet"));     // 源包名称
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "code"));       // 参数代号
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "name"));       // 参数名称
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "type"));       // 类型
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "channel"));    // 通道号
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "byteRange"));  // 路号(Byte)
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "bitRange"));   // 位号(bit)
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "unit"));       // 单位
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "precision"));  // 精度
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "rawType"));    // 源码数据类型
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "formula"));    // 公式
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "range"));      // 正常值范围
            // 系数a~j
            for (char coeff = 'a'; coeff <= 'j'; coeff++) {
                row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "coeff_" + coeff));
            }
            // 复杂系数a~e
            for (char c = 'a'; c <= 'e'; c++) {
                row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "complex_" + c));
            }

            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "bitWidth")); // 数据位宽
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "freq"));     // 帧频
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "endian"));   // 是否大端
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "remark"));   // 备注
            row.createCell(col++).setCellValue("");                                  // 空列
            row.createCell(col++).setCellValue(getTagTextOrEmpty(param, "coeff_a2")); // 第二个系数a
        }

        // 5. 自动调整列宽
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 6. 写入 Excel 文件
        try (FileOutputStream fileOut = new FileOutputStream("telemetry.xlsx")) {
            workbook.write(fileOut);
        }

        workbook.close();
        System.out.println("✅ 导出完成：telemetry.xlsx");
    }
    // 获取子标签内容，如果不存在则返回空字符串
    private static String getTagTextOrEmpty(Element parent, String tagName) {
        Elements elems = parent.getElementsByTag(tagName);
        if (elems.isEmpty()) {
            return "";
        }
        return elems.first().text();
    }

}
