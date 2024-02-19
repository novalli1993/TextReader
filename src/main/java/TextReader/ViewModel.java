package TextReader;

import TextReader.model.TmpFileReader;
import TextReader.model.TxtFileInput;
import TextReader.model.data.Book;
import TextReader.model.data.Chapter;
import TextReader.model.data.Paragraph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewModel {
    private final String tmpPath = System.getProperty("user.dir") + "/src/main/resources/books";
    String name;
    TxtFileInput txtFileInput = new TxtFileInput();
    TmpFileReader tmpFileReader = new TmpFileReader(tmpPath);

    public List<String> ListBooks() {
        File[] books = new File(tmpPath).listFiles();
        List<String> names = new ArrayList<>();
        if (books != null) {
            for (File book : books) {
                names.add(book.getName());
            }
        }
        return names;
    }

    public ObservableList<Chapter> catalog(String filePath) {
        File file = new File(filePath);
        if (file.isFile()) {
            name = file.getName().substring(0, file.getName().lastIndexOf("."));
            if (!new File(tmpPath + "/" + name + "/info.json").exists()) {
                Book book = txtFileInput.bookFormat(filePath);
                tmpFileReader.tmpSaver(book);
            }
        } else {
            name = file.getName();
        }

        List<Chapter> chapterList = new ArrayList<>();
        Map<String, String> info = tmpFileReader.infoRead(name);
        for (int i = 0; i < Integer.parseInt(info.get("chapters")); i++) {
            chapterList.add(tmpFileReader.chapterRead(name, i));
        }
        return FXCollections.observableArrayList(chapterList);
    }

    public String paragraphs(Chapter selectedChapter) {
        List<Paragraph> paragraphs = tmpFileReader.contentRead(name, selectedChapter.getNumber());
        StringBuilder sb = new StringBuilder();
        for (Paragraph paragraph : paragraphs) {
            sb.append(paragraph.getText());
            sb.append("\n");
        }
        return sb.toString();
    }
}

