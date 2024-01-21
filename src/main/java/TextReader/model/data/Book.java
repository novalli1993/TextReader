package TextReader.model.data;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
    private String name;
    private String filePath;
    private List<Paragraph> paragraphs;
    private List<Chapter> chapters;

    public Book(String name, String filePath, List<Paragraph> paragraphs, List<Chapter> chapters) {
        this.name = name;
        this.filePath = filePath;
        this.paragraphs = paragraphs;
        this.chapters = chapters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
