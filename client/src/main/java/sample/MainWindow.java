package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {

    @FXML
    ListView listView;

    public void tryDownload(ActionEvent actionEvent) {
    }

    public void initialize(URL location, ResourceBundle resources) {
        listView.getItems().addAll("Test", "Test2", "Test3", "Test4");
    }
}
