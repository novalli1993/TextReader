package TextReader.model;

import TextReader.model.data.Book;
import TextReader.model.data.Chapter;
import TextReader.model.data.Paragraph;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class TmpFileReader {
    private final String tmpPath;

    public TmpFileReader(String tmpPath) {
        this.tmpPath = tmpPath;
    }

    public Chapter chapterRead(String name, int chapterNum) {
        File formatChapters = new File(tmpPath + "/" + name + "/chapters.dat");
        Chapter chapter;
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(new FileInputStream(formatChapters));
            do {
                chapter = (Chapter) in.readObject();
            } while (chapter.getNumber() != chapterNum);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return chapter;
    }

    public List<Paragraph> contentRead(String name, int chapterNum) {
        File formatParas = new File(tmpPath + "/" + name + "/content.dat");
        Chapter chapter;
        ObjectInputStream in;
        List<Paragraph> paragraphs = new ArrayList<>();
        try {
            chapter = chapterRead(name, chapterNum);
            in = new ObjectInputStream(new FileInputStream(formatParas));
            while (true) {
                try {
                    Paragraph paragraph = (Paragraph) in.readObject();
                    if (paragraph.getNumber() >= chapter.getStart() && paragraph.getNumber() <= chapter.getEnd()) {
                        paragraphs.add(paragraph);
                    }
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return paragraphs;
    }

    public Map<String, String> infoRead(String name) {
        String filePath = tmpPath + "/" + name + "/info.json";
        Map<String, String> info = new HashMap<>();

        try (FileReader fileReader = new FileReader(filePath)) {
            char[] buffer = new char[1024];
            StringBuilder jsonText = new StringBuilder();
            int charsRead;

            while ((charsRead = fileReader.read(buffer)) != -1) {
                jsonText.append(buffer, 0, charsRead);
            }

            JSONObject jsonObject = new JSONObject(jsonText.toString());

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = (String) jsonObject.get(key);
                info.put(key, value);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

    public void tmpSaver(Book book) {
        String formatPath = tmpPath + "/" + book.getName();

        if (!new File(formatPath).exists()) {
            if (!new File(formatPath).mkdirs()) {
                // TODO 警告信息
                System.out.println("创建目录失败！");
            }
        }

        // 保存content和chapter文件
        File formatParas = new File(formatPath + "/content.dat");
        File formatChapters = new File(formatPath + "/chapters.dat");
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
        String filePath = formatPath + "/info.json";
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
