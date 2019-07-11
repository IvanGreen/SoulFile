package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {

    @FXML
    TextArea chatLog;

    @FXML
    ListView listView;

    public void tryDownload(ActionEvent actionEvent) {
    }

    public void initialize(URL location, ResourceBundle resources) {
        chatLog.appendText("Welcome in SoulFile program!");
        listView.getItems().addAll("Test", "Test2", "Test3", "Test4"); //Todo
    }
}
