package sample;

import GreenCode.common.AbstractMessage;
import GreenCode.common.AuthenticationCommand;
import GreenCode.common.AuthenticationRequest;
import GreenCode.common.User;
import javafx.application.Platform;
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
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    VBox authorization;

    @FXML
    TextField login;

    @FXML
    PasswordField password;

    private static User user;

    public void auth(ActionEvent actionEvent){
        Network.sendMsg(new AuthenticationRequest(login.getText(),password.getText()));
        System.out.println("AuthenticationRequest send with: " + login.getText() + " / " + password.getText());
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();
        Thread t = new Thread(() -> {
                while (user == null){
                    try {
                        AbstractMessage am = Network.readObject();
                        if (am instanceof AuthenticationCommand){
                            AuthenticationCommand ac = (AuthenticationCommand) am;
                            String nickname = ac.getNickname();
                            if (nickname != null){
                                user = new User(nickname);
                                System.out.println("Successfully connection register person:  " + nickname);
                                updateUI(this::getMainWindow);
                            } else {
                                updateUI(this::getLoginExceptionWindow);
                            }
                        }
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    } finally {
                        Network.stop();
                    }
                }
        });
        t.setDaemon(true);
        t.start();
    }

    private void updateUI(Runnable r) {
        if (Platform.isFxApplicationThread()){
            r.run();
        } else {
            Platform.runLater(r);
        }
    }
}
