package poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelGenerator {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static void main(String[] args) {
        // 创建一些示例数据
        List<ExcelData> dataList = new ArrayList<>();
        for (int i = 0; i < 1000000; i++){
            dataList.add(new ExcelData("Row1-Col1", i, new Date()));
        }

        // 生成 Excel 文件
        String filePath = "generated_excel1.xlsx";
        generateExcel(filePath, dataList);
    }

    public static void generateExcel(String filePath, List<ExcelData> dataList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Column1");
        headerRow.createCell(1).setCellValue("Column2");
        headerRow.createCell(2).setCellValue("Column3");

        // 创建日期格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        // 填充数据
        int rowNum = 1;
        for (ExcelData data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getColumn1());
            row.createCell(1).setCellValue(data.getColumn2());
            row.createCell(2).setCellValue(dateFormat.format(data.getColumn3()));
        }

        // 写入文件
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
            workbook.close();
            System.out.println("Excel 文件生成成功：" + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ExcelData {
    private String column1;
    private Integer column2;
    private Date column3;

    public ExcelData(String column1, Integer column2, Date column3) {
        this.column1 = column1;
        this.column2 = column2;
        this.column3 = column3;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public Integer getColumn2() {
        return column2;
    }

    public void setColumn2(Integer column2) {
        this.column2 = column2;
    }

    public Date getColumn3() {
        return column3;
    }

    public void setColumn3(Date column3) {
        this.column3 = column3;
    }
}
