package TextReader.model;

import TextReader.model.data.Chapter;
import TextReader.model.data.Paragraph;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class LocalTxtReader {
    private final String tmpPath;
    public LocalTxtReader(String tmpPath) {
        this.tmpPath = tmpPath;
    }
    public Chapter chapterRead(String name, int chapterNum) {
    File formatChapters = new File(tmpPath+"/"+name+"/chapters.dat");
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
        File formatParas = new File(tmpPath+"/"+name+"/content.dat");
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
        String filePath = tmpPath+"/"+name+"/info.json";
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
}
