
package WebPackage;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.ResourceBundle;

public class FXMLDocumentController implements Initializable {
    @FXML
    private TabPane tabPane;
    @FXML
    Label downloadStatusLabel;
    @FXML
    private Menu historyMenu = new Menu();
    @FXML
    private AnchorPane downloadAnchorPane;
    @FXML
    private ListView<String> favList;

    @FXML
    private Button printButton;
    private TextField urlSearch;
    private class DownloadTask extends Task<Void> {

        private String url;

        public DownloadTask(String url) {
            this.url = url;
        }

        @Override
        protected Void call() throws Exception {
            String ext = url.substring(url.lastIndexOf("."), url.length());
            URLConnection connection = new URL(url).openConnection();
            long fileLength = connection.getContentLengthLong();

            try (InputStream is = connection.getInputStream();
                    OutputStream os = Files.newOutputStream(Paths.get("downloadedfile" + ext))) {

                long nread = 0L;
                byte[] buf = new byte[8192];
                int n;
                while ((n = is.read(buf)) > 0) {
                    os.write(buf, 0, n);
                    nread += n;
                    updateProgress(nread, fileLength);
                }
            }

            return null;
        }

        @Override
        protected void failed() {
            System.out.println("failed");
            downloadStatusLabel.setText("Download failed!");
        }

        @Override
        protected void succeeded() {
            System.out.println("downloaded");
            downloadStatusLabel.setText("File download complete");
        }
    }
    private Parent createContent() {
        VBox root = new VBox();
        root.setPrefSize(300, 400);

        TextField fieldURL = new TextField();
        fieldURL.setPromptText("⚭ enter download link here");
        root.getChildren().addAll(fieldURL);

        fieldURL.setOnAction(event -> {
            downloadStatusLabel.setText("Downloading...");
            Task<Void> task = new DownloadTask(fieldURL.getText());
            ProgressBar progressBar = new ProgressBar();
            progressBar.setPrefWidth(350);
            progressBar.progressProperty().bind(task.progressProperty());
            root.getChildren().add(progressBar);

            fieldURL.clear();

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });

        return root;
    }


    ObservableList<String> histItems = FXCollections.observableArrayList ();


    class NewTab{
        private final Tab newTab;
        private final AnchorPane smallAnchor;
        private final ToolBar toolBar;
        private final Label label;
        private final MenuBar menuBar;
        private final Menu bookmarksMenu, settingsMenu, helpMenu;
        private final HBox hBox;
        private final TextField urlBox;
        private final Button goButton;
        private final Button backButton;
        private final Button forwardButton;
        private final Button reloadButton;
        private final BorderPane borderPane;
        private MyBrowser myBrowser;

        public NewTab(){
            newTab = new Tab();
            smallAnchor = new AnchorPane();
            toolBar = new ToolBar();
            label = new Label();
            menuBar = new MenuBar();
            bookmarksMenu = new Menu();
            settingsMenu = new Menu();
            helpMenu = new Menu();
            hBox = new HBox();
            urlBox = new TextField();
            urlSearch = urlBox;
            goButton = new Button();
            backButton = new Button();
            forwardButton = new Button();
            reloadButton = new Button();
            borderPane = new BorderPane();
        }
        
        public Tab createTab(){
            goButton.setText("Go");
            newTab.setText("New Tab");
            
            backButton.setText("◁");
            forwardButton.setText("▷");
                    toolBar.getItems().addAll(backButton, forwardButton);
                    toolBar.setPrefHeight(40);
                    toolBar.setPrefWidth(549);
                AnchorPane.setTopAnchor(toolBar, 0.0);
                AnchorPane.setLeftAnchor(toolBar, 0.0);
                AnchorPane.setRightAnchor(toolBar, 0.0);
                smallAnchor.getChildren().add(toolBar);
                
                bookmarksMenu.setText("Bookmarks");
                menuBar.getMenus().addAll(bookmarksMenu);
                    menuBar.setPrefWidth(190);
                    menuBar.setPrefHeight(40);
                    menuBar.setPadding(new Insets(4,0,0,0));
                AnchorPane.setRightAnchor(menuBar, 7.0);
                
                                  
                urlBox.setPromptText("🔎 enter une url");
                urlBox.setPrefHeight(30);
                urlBox.setPrefWidth(391);
                goButton.setPrefHeight(30);
                goButton.setPrefWidth(32);
                reloadButton.setText("↺");
                reloadButton.setPrefHeight(30);
                reloadButton.setPrefWidth(24);
                
                hBox.getChildren().addAll(urlBox, goButton, reloadButton);
                hBox.setSpacing(5.0);
                AnchorPane.setTopAnchor(hBox, 7.0);
                AnchorPane.setLeftAnchor(hBox, 80.0);
                smallAnchor.getChildren().add(hBox);
                

                AnchorPane.setTopAnchor(label, 10.0);
                AnchorPane.setLeftAnchor(label, 520.0);
                smallAnchor.getChildren().add(label);
                
                
                AnchorPane.setTopAnchor(borderPane, 40.0);
                AnchorPane.setBottomAnchor(borderPane, 0.0);
                AnchorPane.setLeftAnchor(borderPane, 0.0);
                AnchorPane.setRightAnchor(borderPane, 0.0);
                smallAnchor.getChildren().add(borderPane);
        
                newTab.setContent(smallAnchor);
                newTab.setOnClosed((Event arg) -> {
                    newTabBtnPosLeft();
                    myBrowser.closeWindow();
                });
                
                backButton.setOnMouseClicked((MouseEvent me) -> {

                    myBrowser.goBack();
                    label.setText("");
                });

                
                forwardButton.setOnMouseClicked((MouseEvent me) -> {
                    System.out.println("Forward button has been pressed.");
                    myBrowser.goForward();
                    label.setText("");
                });
            printButton.setOnMouseClicked((MouseEvent me) -> {
                myBrowser.print();
            });

                AnchorPane.setTopAnchor(tabPane, 0.0);
                AnchorPane.setBottomAnchor(tabPane, 0.0);
                AnchorPane.setLeftAnchor(tabPane, 0.0);
                AnchorPane.setRightAnchor(tabPane, 0.0);
                

                goButton.setOnAction((ActionEvent e) -> {
                    goButtonPressed();
                });
                
                reloadButton.setOnAction((ActionEvent e) -> {
                    myBrowser.reloadWebPage();
                });
                
                urlBox.setOnAction((ActionEvent e) -> {
                    goButtonPressed();
                });
                
                return newTab;
        }
        
        public void goToURL(String urlStr){
            myBrowser = new MyBrowser(urlStr);
            borderPane.setCenter(myBrowser);
        }
        
        void goButtonPressed(){
            label.setText("");
            String urlStr;
            if(urlBox.getText() != null && !urlBox.getText().isEmpty()){
               if(!urlBox.getText().startsWith("http://")){
                    urlStr = "http://" + urlBox.getText();
                }
                else if(!urlBox.getText().startsWith("http://www.")){
                    urlStr = "http://www." + urlBox.getText();
                }
                else{
                    urlStr = urlBox.getText();
                }
                
                myBrowser = new MyBrowser(urlStr);
                borderPane.setCenter(myBrowser);
            }
            else{
                label.setText("You didn't enter anything : (");
            }
        }
        
        public void setTabBackground(String imageFileLocation){
            ImageView iv = new ImageView();
            Image img = new Image(imageFileLocation);
            iv.setImage(img);
            borderPane.setCenter(iv);
        }
        
        public void setTabContent(MyBrowser passedBroser){
            borderPane.setClip(passedBroser);
        }
        
        
        class MyBrowser extends Region{
            WebView browser = new WebView();
            final WebEngine webEngine = browser.getEngine();
            WebHistory history = webEngine.getHistory();
            public WebView getView(){
                return browser;
            }

                public MyBrowser(String url) {
                    webEngine.getLoadWorker().stateProperty().addListener(
                    new ChangeListener<State>() {
                        public void changed(ObservableValue ov, State oldState, State newState) {
                            ProgressIndicator progInd = new ProgressIndicator(-1.0);
                            progInd.setPrefHeight(17);
                            progInd.setPrefWidth(25);
                            newTab.setGraphic(progInd);
                            reloadButton.setText("X");
                            reloadButton.setOnAction((ActionEvent e) -> {
                                myBrowser.closeWindow();
                                newTab.setText("Aborted!");
                                label.setText("You have aborted loading the page.");
                                newTab.setGraphic(null);
                            });
                            
                            if (newState == State.SUCCEEDED) {
                                label.setText("");
                                newTab.setText(webEngine.getTitle());
                                urlBox.setText(webEngine.getLocation());
                                newTab.setGraphic(loadFavicon(url));
                                reloadButton.setText(("↺"));
                                reloadButton.setOnAction((ActionEvent e) -> {
                                    myBrowser.reloadWebPage();
                                });

                                EventListener listener = new EventListener() {
                                    public void handleEvent(Event ev) {
                                        System.out.println("You pressed on a link");
                                    }
                                };

                                Document doc = webEngine.getDocument();
                                NodeList el = doc.getElementsByTagName("a");
                                for (int i = 0; i < el.getLength(); i++) {
                                }
                            }
                        }                       
                    });
                                
                    history.getEntries().addListener(new 
                        ListChangeListener<Entry>() {
                            @Override
                            public void onChanged(Change<? extends Entry> c) {
                                c.next();
                                for (Entry e : c.getRemoved()) {
                                    historyMenu.getItems().remove(e.getUrl());
                                }
                                for (Entry e : c.getAddedSubList()) {
                                    MenuItem menuItem = new MenuItem(e.getUrl().replace(e.getUrl().substring(24), ""));
                                    histObj.addHist(e.getUrl(), "hist.txt");
                                    histItems.add(e.getUrl());
                                    menuItem.setGraphic(loadFavicon(e.getUrl()));
                                    menuItem.setOnAction((ActionEvent ev) -> {
                                        NewTab aTab = new NewTab();
                                        aTab.goToURL(e.getUrl());
                                        Tab tab = aTab.createTab();
                                        tabPane.getTabs().add(tab);
                                        tabPane.getSelectionModel().select(tab);
                                        newTabBtnPosRight();
                                    });
                                    historyMenu.setText(LocalDate.now().toString());
                                    historyMenu.getItems().add(menuItem);
                                }
                            }
                        }
                    );

                    webEngine.setCreatePopupHandler(
                            (PopupFeatures config) -> {
                                browser.setFontScale(0.8);
                                if (!getChildren().contains(browser)) {
                                    getChildren().add(browser);
                                }
                                return browser.getEngine();
                    });
                    
                    final WebView smallView = new WebView();
                    webEngine.load(url);
                    getChildren().add(browser);
                }
                

                public void goBack(){ 
                  final WebHistory history = webEngine.getHistory();
                  ObservableList<Entry> entryList = history.getEntries();
                  int currentIndex = history.getCurrentIndex();

                  Platform.runLater(() -> 
                  {
                    history.go(entryList.size() > 1 
                      && currentIndex > 0
                            ? -1
                            : 0); 
                  });        
                }
                
                public void goForward(){
                  final WebHistory history = webEngine.getHistory();   
                  ObservableList<Entry> entryList = history.getEntries();
                  int currentIndex = history.getCurrentIndex();

                  Platform.runLater(() -> 
                  {
                    history.go(entryList.size() > 1
                      && currentIndex < entryList.size() - 1
                                    ? 1
                                    : 0); 
                  });    
                }
                
                public ImageView loadFavicon(String location) {
                    try {
                      String faviconUrl;
                      if(webEngine.getTitle().equalsIgnoreCase("Google")){
                          faviconUrl = "https://www.google.com/s2/favicons?domain_url=www.gmail.com";
                      }
                      else{
                          faviconUrl = String.format("http://www.google.com/s2/favicons?domain_url=%s", URLEncoder.encode(location, "UTF-8"));
                      }
                        
                        Image favicon = new Image(faviconUrl, true);
                        if(favicon.equals(new Image("http://www.google.com/s2/favicons?domain_url=abc"))){
                            ImageView iv_default = new ImageView(new Image("file:Resources/home.png"));
                            return iv_default;
                        }else{
                            ImageView iv = new ImageView(favicon);
                            return iv;
                        }
                    } catch (UnsupportedEncodingException ex) {
                      throw new RuntimeException(ex);
                    }
                }
            public void print() {

                Printer printer = Printer.getDefaultPrinter();
                PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
                double scaleX = pageLayout.getPrintableWidth() / browser.getBoundsInParent().getWidth();
                double scaleY = pageLayout.getPrintableHeight() / browser.getBoundsInParent().getHeight();
                browser.getTransforms().add(new Scale(scaleX, scaleY));

                PrinterJob job = PrinterJob.createPrinterJob();
                if (job != null) {
                    boolean success = job.printPage(browser);
                    if (success) {
                        job.endJob();
                    }
                }
                double scaleX1 = pageLayout.getPrintableWidth() * browser.getBoundsInParent().getWidth();
                double scaleY1 = pageLayout.getPrintableHeight() * browser.getBoundsInParent().getHeight();
                browser.getTransforms().add(new Scale(scaleX1, scaleY1));
            }
                
                public void closeWindow(){
                    browser.getEngine().load(null);
                    browser = null;
                }
                
                public void reloadWebPage(){
                    webEngine.reload();
                }

                @Override protected void layoutChildren() {
                    double w = getWidth();
                    double h = getHeight();
                    layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
                }

                @Override protected double computePrefWidth(double height) {
                    return 750;
                }

                @Override protected double computePrefHeight(double width) {
                    return 500;
                }
            }
    }
    
    private double newTabLeftPadding = 102.0;
  
    @FXML
    private Button newTabBtn;
    
    @FXML
    private void newTabFunction(ActionEvent event){  
          NewTab aTab = new NewTab();
          Tab tab = aTab.createTab();
          tabPane.getTabs().add(tab);
          tabPane.getSelectionModel().select(tab);
          newTabBtnPosRight();
    }
   
    private void newTabBtnPosRight(){   
        newTabLeftPadding += 91;
        AnchorPane.setLeftAnchor(newTabBtn, newTabLeftPadding++);
    }
    
    private void newTabBtnPosLeft(){
        newTabLeftPadding -= 91;
        AnchorPane.setLeftAnchor(newTabBtn, newTabLeftPadding--);
        if(newTabLeftPadding < 102.0){
            System.out.println("All tabs closed.");
            Platform.exit();
        }
    }
    
    @FXML
    private Label homeLabel;
    @FXML
    private void homeBtnHover(){
        homeLabel.setText("Home");
    }
    
    @FXML
    private void homeBtnHoverExit(){
        homeLabel.setText("");
    }
    
    @FXML
    private Label downloadLabel;
    @FXML
    private void downloadBtnHover(){
        downloadLabel.setText("Downloads");
    }
    @FXML
    private void downloadBtnHoverExit(){
        downloadLabel.setText("");
    }
    @FXML
    private Label bookmarkLabel;
    @FXML
    private void bookmarkBtnHover(){
        bookmarkLabel.setText("Bookmarks");
    }
    @FXML
    private void bookmarkBtnHoverExit(){
        bookmarkLabel.setText("");
    }
    
    @FXML
    private Button downloadButton;
    @FXML
    private Button bookmarkButton;
    
    private NewTab aTab = new NewTab();
    
    HistoryObject histObj;
    FavObject favObj;
    
    @FXML
    ListView historyList;





    @FXML
    Label historyLabel;
    @FXML
    Label FavWind;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        histObj = new HistoryObject();
        favObj = new FavObject();
        Tab tab = aTab.createTab();
        tab.setText("Home Tab");
        tabPane.getTabs().add(tab);
        
        ImageView iv = new ImageView();
        Image img = new Image("file:Resources/home.png");
        iv.setImage(img);
        iv.setFitHeight(21);
        iv.setFitWidth(20);
        homeBtn.setGraphic(iv);


        
        ImageView iv2 = new ImageView();
        Image img2 = new Image("file:Resources/downloadIcon.png");
        iv2.setImage(img2);
        iv2.setFitHeight(21);
        iv2.setFitWidth(20);
        downloadButton.setGraphic(iv2);
        
        ImageView iv3 = new ImageView();
        Image img3 = new Image("file:Resources/Bookmark-512.png");
        iv3.setImage(img3);
        iv3.setFitHeight(21);
        iv3.setFitWidth(20);
        bookmarkButton.setGraphic(iv3);

        ImageView iv5 = new ImageView();
        Image img5 = new Image("file:Resources/printicon.png");
        iv5.setImage(img5);
        iv5.setFitHeight(21);
        iv5.setFitWidth(20);
        printButton.setGraphic(iv5);

        ObservableList<String> HistItems = FXCollections.observableArrayList ();
        ArrayList<HistoryObject> ar = new ArrayList();
        ar = histObj.getAllHistory("hist.txt");
        for (int i = 0; i < ar.size(); i++) {
            HistItems.add(ar.get(i).url);
        }
        historyList.setItems(HistItems);


        ObservableList<String> FavItems = FXCollections.observableArrayList ();
        ArrayList<FavObject> ar2 = new ArrayList();
        ar2 = favObj.getAllFavs("fav.txt");
        for (int i = 0; i < ar2.size(); i++) {
            FavItems.add(ar2.get(i).url);
        }
        favList.setItems(FavItems);
        
        Parent parent = createContent();
        downloadAnchorPane.getChildren().add(parent);
        downloadAnchorPane.setVisible(false);
        historyAnchorPane.setVisible(false);
        FavoAnchorPane.setVisible(false);
        
        
    }
    

    @FXML
    private void backgroundImgFunction(){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(stage);
        System.out.println("You chose this file: " + file.getAbsolutePath());
        aTab.setTabBackground("file:" + file.getAbsolutePath()); 
    }
    
    
    @FXML
    private Button homeBtn;
    
    @FXML
    private void createHomeTab(){
        System.out.println("Home button pressed.");
        
        aTab = new NewTab();
        aTab.setTabBackground("file:Resources/b46c8e1cde764e377f0ed9399e6380a6.jpg");        
        Tab tab = aTab.createTab();
        tab.setText("Home Tab");
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        newTabBtnPosRight();
    }

    int k = 0;  
    @FXML
    private void downloadButtonFunction(){
        k++;
        if(k%2 == 0){
            System.out.println("Download menu is now hidden.");
            downloadAnchorPane.setVisible(false);
            downloadStatusLabel.setText("");
        }
        else{
            downloadAnchorPane.setVisible(true);
            System.out.println("Download menu is now visible.");
            downloadAnchorPane.setStyle("-fx-background-color: gray;");
        }
    }

    int m = 0;
    int m2=0;
    @FXML
    private AnchorPane historyAnchorPane;
    @FXML
    private AnchorPane FavoAnchorPane;
    @FXML
    private void BookmarkButtom(){
        for (String s:favList.getItems()) {
            if(urlSearch.getText().equals(s)){
                favList.getItems().remove(urlSearch.getText());
                return;
            }
        }
        favObj.addFav(urlSearch.getText(),"fav.txt");
        favList.getItems().add(urlSearch.getText());
    }
    
    @FXML
    private void FavWindFunction(){
        m2++;
        if(m2%2 == 0){
            FavoAnchorPane.setVisible(false);
        }
        else{
            FavoAnchorPane.setVisible(true);
        }
        System.out.println("Bookmark button pressed.");
    }


    @FXML
    private void historyLabelFunction(){
        m++;
        if(m%2 == 0){
            historyAnchorPane.setVisible(false);
        }
        else{
            historyAnchorPane.setVisible(true);
            System.out.println("Showing history options");
        }
    }
}
    
