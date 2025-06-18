package xml;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

public class FixAndParseXML {

    public static void main(String[] args) {
        String inputPath = "src/main/java/xml/text.xml";
        String outputPath = "src/main/java/xml/text1.xml";

        try (InputStream inputStream = new FileInputStream(inputPath);
             OutputStream outputStream = new FileOutputStream(outputPath)) {

            // 使用 JTidy 清洗 XML
            Tidy tidy = new Tidy();
            tidy.setXmlTags(true);
            tidy.setInputEncoding("UTF-8");
            tidy.setOutputEncoding("UTF-8");
            tidy.setQuiet(true);
            tidy.setShowWarnings(false);
            tidy.setIndentContent(true);

            Document fixedDoc = tidy.parseDOM(inputStream, outputStream);

            // 读取修复后的 XML 文件进行标准 DOM 解析
            Document standardDoc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new File(outputPath));

            // 示例：打印所有 <data> 节点的文本内容
            NodeList dataNodes = standardDoc.getElementsByTagName("data");
            for (int i = 0; i < dataNodes.getLength(); i++) {
                System.out.println("data: " + dataNodes.item(i).getTextContent());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
