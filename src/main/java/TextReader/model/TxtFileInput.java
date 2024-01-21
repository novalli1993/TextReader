package TextReader.model;

import TextReader.model.data.Book;
import TextReader.model.data.Chapter;
import TextReader.model.data.Paragraph;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class TxtFileInput {
    private final String tmpPath;
    private String firstTitle = "前言";

    public TxtFileInput(String tmpPath) {
        this.tmpPath = tmpPath;
    }
    public void bookFormat(String filePath) {
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
            while (line != null){
                while (line.isEmpty()) line = br.readLine();
                Paragraph paragraph = new Paragraph(paraCount, line);
                paragraph.formatParagraph();
                if (paragraph.isTitle()){
                    if (!chapters.isEmpty()) {
                        chapters.get(chapters.size()-1).setEnd(paraCount-1);
                    }
                    chapters.add(new Chapter(chapCount, paragraph.getText(), paraCount, paraCount));
                    chapCount ++;
                } else {
                    if (paraCount == 0){
                        chapters.add(new Chapter(chapCount, firstTitle, paraCount, paraCount));
                        chapCount ++;
                    }
                    paragraphs.add(paragraph);
                    paraCount++;
                }
                line = br.readLine();
            }
            if (chapters.get(chapters.size()-1).getTitle().equals(firstTitle)) {
                chapters.clear();
                int chapSlip = 50;
                for (int i = 0; i <= paraCount/chapSlip; i++) {
                    chapters.add(new Chapter(i, "", i * chapSlip, Math.min(i * chapSlip+chapSlip-1, paraCount)));
                }
            } else {
                chapters.get(chapters.size()-1).setEnd(paraCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tmpSaver(new Book(name, filePath, paragraphs, chapters));
    }

    private void tmpSaver(Book book) {
        String formatPath = tmpPath+"/"+book.getName();
        int result = 1+2+4;

        if (!new File(formatPath).exists()) {
            if (!new File(formatPath).mkdirs()) {
                // TODO 警告信息
                System.out.println("创建目录失败！");
            }
        }

        // 保存content和chapter文件
        File formatParas = new File(formatPath+"/content.dat");
        File formatChapters = new File(formatPath+"/chapters.dat");
        try {
            if (!formatParas.exists()) {
                if (!formatParas.createNewFile()) {
                    // TODO 警告信息
                    System.out.println("创建content文件失败！");
                }
            }
            if (!formatChapters.exists()) {
                if (!formatChapters.createNewFile()) {
                    // TODO 警告信息
                    System.out.println("创建chapters文件失败！");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Chapter> chapters = book.getChapters();
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(formatChapters))) {
            for (Chapter chapter : chapters) {
                try {
                    output.writeObject(chapter);
                } catch (IOException e) {
                    throw new RuntimeException("Error writing chapter to file: " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Paragraph> paragraphs = book.getParagraphs();
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(formatParas))) {
            for (Paragraph paragraph : paragraphs) {
                try {
                    output.writeObject(paragraph);
                } catch (IOException e) {
                    throw new RuntimeException("Error writing paragraph to file: " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 保存info文件
        String filePath = formatPath+"/info.json";
        JSONObject info = new JSONObject();
        try {
            info.put("name", book.getName());
            info.put("filepath", book.getFilePath());
            info.put("chapters", Integer.toString(book.getChapters().size()));
            info.put("paragraphs", Integer.toString(book.getParagraphs().size()));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(info.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}