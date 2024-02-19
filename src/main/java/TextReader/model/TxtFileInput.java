package TextReader.model;

import TextReader.model.data.Book;
import TextReader.model.data.Chapter;
import TextReader.model.data.Paragraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TxtFileInput {
    public Book bookFormat(String filePath) {
        String name = new File(filePath).getName();
        name = name.substring(0, name.lastIndexOf("."));
        List<Paragraph> paragraphs = new ArrayList<>();
        List<Chapter> chapters = new ArrayList<>();
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
            /* 读入TXT文件 */
            File file = new File(filePath); // 要读取以上路径的input.txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = br.readLine();
            int paraCount = 0;
            int chapCount = 0;
            String firstTitle = "前言";
            while (line != null) {
                while (line.isEmpty()) line = br.readLine();
                Paragraph paragraph = new Paragraph(paraCount, line);
                paragraph.formatParagraph();
                if (paragraph.isTitle()) {
                    if (!chapters.isEmpty()) {
                        chapters.get(chapters.size() - 1).setEnd(paraCount - 1);
                    }
                    chapters.add(new Chapter(chapCount, paragraph.getText(), paraCount, paraCount));
                    chapCount++;
                } else {
                    if (paraCount == 0) {
                        chapters.add(new Chapter(chapCount, firstTitle, paraCount, paraCount));
                        chapCount++;
                    }
                    paragraphs.add(paragraph);
                    paraCount++;
                }
                line = br.readLine();
            }
            if (chapters.get(chapters.size() - 1).getTitle().equals(firstTitle)) {
                chapters.clear();
                int chapSlip = 50;
                for (int i = 0; i <= paraCount / chapSlip; i++) {
                    chapters.add(new Chapter(i, "", i * chapSlip, Math.min(i * chapSlip + chapSlip - 1, paraCount)));
                }
            } else {
                chapters.get(chapters.size() - 1).setEnd(paraCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Book(name, filePath, paragraphs, chapters);
    }
}