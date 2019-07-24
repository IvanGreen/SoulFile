package sample;

import GreenCode.server.DBConnection;
import GreenCode.common.User;
import GreenCode.server.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    VBox authorization;

    @FXML
    TextField login;

    @FXML
    PasswordField password;

    private static User user;

    public void auth(ActionEvent actionEvent){
        if (tryAuth()){
            getMainWindow();
        } else {
            getLoginExceptionWindow();
        }
    }

    private boolean tryAuth() {

        try {
            DBConnection.connect();
            String nickname = DBConnection.getNicknameByLoginAndPassword(login.getText(),password.getText());
                if (nickname != null){
                    user = new User(nickname);
                    System.out.println("Successfully connection register person:  " + nickname);
                    return true;
                }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.disconnect();
        }

        System.out.println("DB Connection false;");
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

    private void getLoginExceptionWindow() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/loginException.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static User getUser() {
        return user;
    }
}
