package TextReader.model.database;

import TextReader.model.data.Book;
import TextReader.model.data.Chapter;
import TextReader.model.data.Paragraph;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

public class SaveFormattedText {
    public static void  tmpSaver(Book book, String tmpPath) {
        String formatPath = tmpPath+"/"+book.getName();

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
