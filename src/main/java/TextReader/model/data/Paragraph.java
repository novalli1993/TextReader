package TextReader.model.data;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Paragraph implements Serializable {
    private int number;
    private String text;

    public Paragraph(int number, String text) {
        this.number = number;
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isTitle() {
        if (this.getText().contains("。") || this.getText().contains("：“")) return false;
        String r = "\\s*\\S*(第[0-9|零一二三四五六七八九十壹贰叁肆伍陆柒捌玖拾百千万]+[节章卷回话]" +
                "|[c|C]hapter.*[0-9]|☆、.*|[上中下终]卷" +
                "|卷[0-9|零一二三四五六七八九十壹贰叁肆伍陆柒捌玖拾]+" +
                "|[引子|楔子|序][\n\\s]|[Ll][Vv].[0-9]+|－Quiz [0-9]+).*";
        Pattern p = Pattern.compile(r);
        Matcher m = p.matcher(this.getText());
        return m.matches();
    }

    public void formatParagraph() {
        this.text = this.text.replaceAll("\\u3000", " ").trim();
    }

    //to ensure that you get the short description and not the object reference this relates to the listView
    @Override
    public String toString() {
        return this.getText();
    }
}
