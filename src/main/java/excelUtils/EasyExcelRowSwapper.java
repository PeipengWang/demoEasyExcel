package excelUtils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import java.io.*;
import java.util.*;

public class EasyExcelRowSwapper {

    public static void main(String[] args) {
        // 输入文件路径
        String inputPath = "E:\\code\\demoEasyExcel\\generated_excel2.xlsx";
        // 输出文件路径
        String outputPath = "E:\\code\\demoEasyExcel\\generated_excel4.xlsx";
        // 要交换的两行（行号从0开始，不包含表头）
        int row1Index = 1; // 示例：第2行数据（不包含表头）
        int row2Index = 2; // 示例：第行数据（不包含表头）

        try {
            swapRows(inputPath, outputPath, row1Index, row2Index);
            System.out.println("行交换成功！");
        } catch (Exception e) {
            System.err.println("行交换失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 交换Excel文件中的两行
     */
    public static void swapRows(String inputPath, String outputPath, int row1Index, int row2Index) throws IOException {
        // 读取Excel文件内容
        List<List<String>> dataList = readExcel(inputPath);
        
        // 检查行索引是否有效
        if (row1Index < 0 || row1Index >= dataList.size() || 
            row2Index < 0 || row2Index >= dataList.size()) {
            throw new IllegalArgumentException("行索引超出范围");
        }
        
        // 交换两行
        Collections.swap(dataList, row1Index, row2Index);
        
        // 写入新的Excel文件
        writeExcel(outputPath, dataList);
    }

    /**
     * 使用EasyExcel读取Excel文件
     */
    private static List<List<String>> readExcel(String filePath) {
        List<List<String>> dataList = new ArrayList<>();
        
        try (InputStream inputStream = new FileInputStream(filePath)) {
            EasyExcel.read(inputStream, new AnalysisEventListener<List<String>>() {
                @Override
                public void invoke(List<String> data, AnalysisContext context) {
                    dataList.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    // 读取完成后的操作
                }
            }).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return dataList;
    }

    /**
     * 使用EasyExcel写入Excel文件
     */
    private static void writeExcel(String filePath, List<List<String>> dataList) {
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            ExcelWriterBuilder writerBuilder = EasyExcel.write(outputStream);
            ExcelWriterSheetBuilder sheetBuilder = writerBuilder.sheet("Sheet1");
            sheetBuilder.doWrite(dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}    