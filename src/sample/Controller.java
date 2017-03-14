package sample;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Controller {

    @FXML
    WebView webView;
    @FXML
    TextField address;
    @FXML
    Button goBack;
    @FXML
    Button goForward;
    @FXML
    Button reload;
    @FXML
    TabPane tabPane;

    WebEngine webEngine;

    @FXML
    protected void initialize(){
        webEngine = webView.getEngine();
        webEngine.load("http://www.google.com.ua");
        webEngine.getLoadWorker().stateProperty().addListener(
                (ObservableValue<? extends Worker.State> observable,
                 Worker.State oldValue,
                 Worker.State newValue) -> {
                    if( newValue != Worker.State.SUCCEEDED ) {
                        return;
                    }
                    tabPane.getSelectionModel().getSelectedItem().setText(webEngine.getTitle());
                    address.setText(webEngine.getLocation());
                } );
    }

    @FXML
    public void onEnter(ActionEvent event){
        webView = (WebView) tabPane.getSelectionModel().getSelectedItem().getContent();
        webView.getEngine().load(address.getText());
    }

    @FXML
    protected void back(){
        Platform.runLater(() -> {
            webEngine.executeScript("history.back()");
        });
    }

    @FXML
    protected void forward(){
        Platform.runLater(() -> {
            webEngine.executeScript("history.forward()");
        });
    }

    @FXML
    protected void reload(){
        Platform.runLater(() -> {
            webEngine.reload();
        });
    }

    @FXML
    protected void addTab(){
        Tab newTab = new Tab();
        WebView webView = new WebView();
        newTab.setContent(webView);
        newTab.setText("Sample Tab");
        newTab.setOnCloseRequest(e -> {
            WebView tempView;
//            tempView = (WebView) newTab.getContent();
            newTab.setContent(null);
            System.gc();
        });
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
    }
}
