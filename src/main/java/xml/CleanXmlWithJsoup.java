package xml;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CleanXmlWithJsoup {

    public static void main(String[] args) throws IOException {
        File input = new File("src/main/java/xml/text.xml");

        // Jsoup 使用 XML 模式解析（容错强）
        Document doc = Jsoup.parse(input , "UTF-8", "", Parser.xmlParser());
        // 输出清洗后的 XML 内容
        FileWriter writer = new FileWriter("src/main/java/xml/cleaned2.xml");
        writer.write(doc.outerHtml());
        writer.close();

        Elements elements = doc.select("root");
        for (Element element : elements){
            Elements data = element.select("data");
            System.out.println(data.text());

        }
        System.out.println("✅ 清洗完成，结果已写入 cleaned.xml");
    }
}
