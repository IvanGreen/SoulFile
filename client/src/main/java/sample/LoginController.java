package sample;

import GreenCode.server.DBconnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    VBox authorization;

    @FXML
    TextField login;

    @FXML
    PasswordField password;

    public void auth(ActionEvent actionEvent){
        if (tryAuth()){
            getMainWindow();
        }
    }

    private boolean tryAuth() {
        try {
            DBconnection.connect();
            String nickname = DBconnection.getNicknameByLoginAndPassword(login.getText(),password.getText());
            if (nickname != null){
                System.out.println("Successfully connection register person:  " + nickname);
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DBconnection.disconnect();
        }
        return false;
    }


    private void getMainWindow(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/mainWindow.fxml"));
            Scene scene = new Scene(root);
            ((Stage)authorization.getScene().getWindow()).setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
