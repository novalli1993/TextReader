import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application {

    private TextArea textArea;
    private double fontSize = 12; // 默认字体大小

    @Override
    public void start(Stage primaryStage) {
        // 创建一个TextArea
        textArea = new TextArea("“妍儿，怎么愁眉苦脸的，有什么苦恼要告诉姐姐啊，不要憋在心里哦。”一个女子的声音。\n" +
                "轻柔而悦耳，很好听。陈暮心下免不了评价一番，不过他可没有睁开眼睛的打算。这对他已经构不成任何吸引力。\n" +
                "他曾见过一个长得极为美丽的女子，狠狠地用高跟鞋在一位和他一起的流浪同伴身上又踩又蹍，嘴里还不断地谩骂着，他从来没想到，如此好听的声音竟然能骂出几乎是他当时认为这个世界上最恶毒的语言。而这件事的起因只不过是他的这位同伴在乞食时不小心弄脏了她的高跟鞋。\n" +
                "当时他的那位同伴只有六岁，而他八岁。他什么也没想，毫不犹豫扑上去，想撞开那位美丽的女子，可惜他的年龄太小。这件事的最终结果是，俩人身上都留下了无数一小块一小块的淤青。他的那位流浪伙伴也是在那年的冬天离开了他，再也醒不过来了，那个冬天太冷。\n" +
                "从那以后，当他听到那些悦耳的女声，他总是不由自主地想到个场景。\n" +
                "陈暮微微皱起眉头，不过旋即又松开了眉头。他告诉自己，不值得为这个声音破坏自己的好心情。\n" +
                "两人交谈的声音却不断地往他的耳朵里钻。\n" +
                "“姐姐，不知怎么回事，我就是做不成蓝皮兔的幻卡。我试了好多次，就是不行我的感知是班上第一呢可整个班上就我一个人还没做出幻卡”脆生生的声音丧气无比。\n" +
                "是个小女孩，陈暮不用睁开眼也能大致猜出小女孩的年龄，肯定不会超过十二岁。\n" +
                "“妍儿和姐姐说说，妍儿做卡片的时候是什么感觉呢”姐姐循循善诱。\n" +
                "“哎，我也说不上来，就是感觉手上的笔不受使唤，明明记得它的结构，可是做的时候，就完不成。”\n" +
                "躺在地上的陈暮蓦地睁开眼睛，虽然背对这姐妹俩，他的耳朵竖得老高，生恐错过任何一个字。小女孩的问题和他几乎完全一样\n" +
                "“妍儿不要急哦。一星幻卡很简单的，妍儿这么聪明，肯定能成功。”姐姐先是鼓励一番妹妹，然后便开始指点其中关键：“妍儿你以前没有制作过卡片，虽然感知很高，但是经验不足也是很正常的。很多老师在这一点也讲得不是很清楚。我们不是用感知来控制材料之间的契合，而是诱导它们之间的契合，它就像个引子，只是引导。而真正发生契合变化的，还是要依靠材料本身的物性，妍儿明白了吗”");

        // 设置TextArea的wrapText属性为false，以显示滚动条
        textArea.setWrapText(true);

        // 设置TextArea的字体大小
        textArea.setStyle("-fx-font-size: " + fontSize + "px;");

        // 设置TextArea的onScroll事件处理器
        textArea.setOnScroll(event -> {
            System.out.println("No");
            // 检查是否按住了Ctrl键
            if (event.isControlDown()) {
                textArea.toBack();
                // 获取滚轮滚动的delta值
                double deltaY = event.getDeltaY();

                // 根据delta值调整字体大小
                fontSize += deltaY * 0.5; // 每次滚动增加或减少0.5个像素

                // 更新TextArea的字体大小
                textArea.setStyle("-fx-font-size: " + fontSize + "px;");
                System.out.println("In");
                event.consume(); // 消费事件，防止默认的滚动行为
            }
        });

        // 设置TextArea的onKeyPressed事件处理器
        textArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.CONTROL) {
                // Ctrl键被按下
            }
        });

        // 设置TextArea的onKeyReleased事件处理器
        textArea.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.CONTROL) {
                // Ctrl键被释放
            }
        });

        // 创建一个VBox作为根布局
        VBox root = new VBox(textArea);
        root.setOnScroll(e -> {
            if (e.isControlDown()) {
                System.out.println("root");
            }
        });

        // 设置场景
        Scene scene = new Scene(root, 300, 200);

        // 设置主舞台
        primaryStage.setTitle("TextArea Zoom with Ctrl Scroll");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}