package TextReader;

import TextReader.model.data.Book;
import TextReader.model.data.Chapter;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class View extends Application {
    Stage stage;
    VBox background;
    Pane viewerPane;
    HBox viewer;
    MenuBar topMenuBar;
    Menu menuOpen;
    Menu menuSetting;
    MenuItem openNew;
    MenuItem fontSizeUp;
    MenuItem fontSizeDown;
    Pane catalogSwitchBar;
    ListView<Chapter> catalog;
    ScrollPane textScroll;
    Separator viewerSeparator;
    Text content;
    String cssFile = "/View.css";
    Font textFont = new Font(18);
    ViewModel viewModel = new ViewModel();
    double scrollBarWidth = 17;
    double scrollResolution = 40;

    private Scene getScene() {
        Scene scene = new Scene(background);
        scene.setOnScroll(e -> {
            if (e.isControlDown()) {
                double change = e.getDeltaY();
                textFont = Font.font(textFont.getSize() + change / scrollResolution);
                content.setFont(textFont);
            }
        });
        scene.getStylesheets().add(cssFile);
        return scene;
    }

    private void setBackground() {
        background = new VBox();
        background.setPrefWidth(700);
        background.setPrefHeight(500);
    }

    private Pane getPane() {
        return new Pane();
    }

    private void setViewerPane() {
        viewerPane = getPane();
        viewerPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        viewerPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
    }

    private void setViewer() {
        viewer = new HBox();
        viewer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        viewer.setPrefWidth(Region.USE_COMPUTED_SIZE);
    }

    private void setMenuBar() {
        topMenuBar = new MenuBar();
    }

    private Menu getMenu(String name) {
        return new Menu(name);
    }

    private void setMenuOpen() {
        menuOpen = getMenu("打开");
    }

    private void setMenuSetting() {
        menuSetting = getMenu("设置");
    }

    private MenuItem getMenuItem(String name) {
        return new MenuItem(name);
    }

    private void setOpenNew() {
        openNew = getMenuItem("打开txt文件");
        openNew.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                content.setText("←请选择章节");
                catalog.setItems(viewModel.catalog(selectedFile.getPath()));
                String name = selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf("."));
                boolean isExisted = false;
                for (MenuItem item : menuOpen.getItems()) {
                    // 防止识别到MenuItem的子类，如SeparatorMenuItem。
                    if (item.getClass().isAssignableFrom(MenuItem.class) && item.getText().equals(name)) isExisted = true;
                }
                if (!isExisted) {
                    MenuItem book = getMenuItem(name);
                    book.setOnAction(event -> {
                        content.setText("←请选择章节");
                        catalog.setItems(viewModel.catalog(name));
                    });
                    menuOpen.getItems().add(0, book);
                }
            }
        });
    }

    private void setFontSizeUp() {
        fontSizeUp = getMenuItem("字号+");
        fontSizeUp.setOnAction(e -> {
            textFont = Font.font(textFont.getSize() + 1);
            content.setFont(textFont);
            e.consume();
        });
    }

    private void setFontSizeDown() {
        fontSizeDown = getMenuItem("字号-");
        fontSizeDown.setOnAction(e -> {
            textFont = Font.font(textFont.getSize() - 1);
            content.setFont(textFont);
            e.consume();
        });
    }

    private Separator getSeparator() {
        return new Separator();
    }

    private SeparatorMenuItem getSeparatorMenuItem() {
        return new SeparatorMenuItem();
    }

    private void setCatalogSwitchBar() {
        catalogSwitchBar = new Pane();
        catalogSwitchBar.setPrefWidth(5);
        catalogSwitchBar.getStyleClass().add("catalogSwitchBar");
        catalogSwitchBar.setOnMouseClicked(e -> {
            if (viewer.getChildren().contains(catalog)) {
                viewer.getChildren().remove(catalog);
            } else {
                viewer.getChildren().add(0, catalog);
            }
            e.consume();
        });
    }

    private void setCatalog(ObservableList<Chapter> observableChapterList) {
        int minSize = 0;
        for (int i = 0; i <= 10; i++) {
            minSize += observableChapterList.get(i).getTitle().length();
        }
        catalog = new ListView<>(observableChapterList);
        catalog.setPrefWidth((double) minSize / 10 * new Text("中").getLayoutBounds().getWidth());
        catalog.setMinWidth(50);
        catalog.setOnMouseClicked(e -> {
            Chapter selectedChapter = catalog.getSelectionModel().getSelectedItem();
            if (selectedChapter != null) {
                content.setText(viewModel.paragraphs(selectedChapter));
            }
            e.consume();
        });
    }

    private void setTextScroll() {
        textScroll = new ScrollPane();
        textScroll.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.CONTROL) {
                textScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                textScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            }
        });
        textScroll.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
            if (e.getCode() == KeyCode.CONTROL) {
                textScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                textScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            }
        });
        textScroll.layoutBoundsProperty().addListener((obs, oldBound, newBound) -> {
            content.setWrappingWidth(newBound.getWidth() - scrollBarWidth);
        });
    }

    private void setViewerSeparator() {
        viewerSeparator = getSeparator();
        viewerSeparator.getStyleClass().add("viewerSeparator");
        viewerSeparator.setOrientation(Orientation.VERTICAL);
        viewerSeparator.setOnMouseDragged(e -> {
            double catalogWidth = catalog.getWidth();
            double textScrollWidth = textScroll.getLayoutBounds().getWidth();
            catalog.setPrefWidth(catalogWidth + e.getX());
            textScroll.setPrefWidth(textScrollWidth - e.getX());
            content.setWrappingWidth(textScroll.getWidth() - scrollBarWidth);
        });
    }

    private void setContent() {
        content = new Text();
        content.prefWidth(Control.USE_COMPUTED_SIZE);
        content.setWrappingWidth(500);
        content.setText("←请选择章节");
        content.setOnScroll(e -> {
            if (e.isControlDown()) {
                double change = e.getDeltaY();
                textFont = Font.font(textFont.getSize() + change / scrollResolution);
                content.setFont(textFont);
            }
        });
    }

    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("TXT阅读器");
        // 设置根节点
        setBackground();
        // 设置菜单栏组件
        setMenuBar();
        setMenuOpen();
        SeparatorMenuItem separatorMenuItem = getSeparatorMenuItem();
        setOpenNew();
        setMenuSetting();
        setFontSizeUp();
        setFontSizeDown();
        // 设置显示区组件
        setViewerPane();

        viewerPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            catalogSwitchBar.setPrefHeight(newVal.doubleValue());
            viewer.setPrefHeight(newVal.doubleValue());
        });
        viewerPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            viewer.setPrefWidth(newVal.doubleValue());
        });

        setCatalogSwitchBar();
        setViewer();
        setCatalog(viewModel.catalog(System.getProperty("user.dir") + "/src/main/resources/TextReader/卡徒.txt"));
        setViewerSeparator();

        setTextScroll();
        setContent();
        // 组装各个组件
        background.getChildren().addAll(topMenuBar, viewerPane);
        VBox.setVgrow(topMenuBar, Priority.NEVER);
        VBox.setVgrow(viewerPane, Priority.ALWAYS);
        topMenuBar.getMenus().addAll(menuOpen, menuSetting);
        // 是否有已缓存书籍
        List<String> bookList = viewModel.ListBooks();
        if (bookList != null) {
            for (String name : bookList) {
                MenuItem book = getMenuItem(name);
                book.setOnAction(e -> {
                    content.setText("←请选择章节");
                    catalog.setItems(viewModel.catalog(name));
                });
                menuOpen.getItems().add(0, book);
            }
        }
        menuOpen.getItems().addAll(separatorMenuItem, openNew);
        menuSetting.getItems().setAll(fontSizeUp, fontSizeDown);
        viewerPane.getChildren().addAll(viewer, catalogSwitchBar);
        viewer.getChildren().addAll(catalog, viewerSeparator, textScroll);
        textScroll.setContent(content);
        HBox.setHgrow(catalog, Priority.SOMETIMES);
        HBox.setHgrow(textScroll, Priority.ALWAYS);

        Scene scene = getScene();

        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.jpg"))));
        stage.setScene(scene);
        stage.show();
        System.gc();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
