package easyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.context.AnalysisContext;
import entity.ExcelData;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EasyExcelDemo {

    // ExcelData 实体类
    public static class ExcelData {
        @ExcelProperty("Column1")
        private String column1;

        @ExcelProperty("Column2")
        private Integer column2;

        @ExcelProperty("Column3")
        @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
        private Date column3;

        // Getters and Setters
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

    public static void main(String[] args) {
        String fileName = "E:\\code\\demoEasyExcel\\generated_excel1.xlsx";
        long l = System.currentTimeMillis();
        List<ExcelData> dataList = new ArrayList<>();
        EasyExcel.read(fileName, ExcelData.class, new ReadListener<ExcelData>() {
            @Override
            public void invoke(ExcelData data, AnalysisContext context) {
                dataList.add(data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                // Do something after all rows are analyzed
            }
        }).sheet().doRead();

        // Print the data
        for (ExcelData data : dataList) {
            System.out.println(data.getColumn1() + ", " + data.getColumn2() + ", " + data.getColumn3());
        }

        long l1 = System.currentTimeMillis();
        System.out.println("消耗时间：" + (l1 - l)/1000);
    }
}
