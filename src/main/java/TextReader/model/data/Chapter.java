package TextReader.model.data;

import java.io.Serializable;

public class Chapter implements Serializable {
    private int number;
    private String title;
    private int start;
    private int end;

    public Chapter(int number, String title, int start, int end) {
        this.number = number;
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return number + ". " + this.getTitle();
    }
}
