package sample;

import GreenCode.common.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {

    @FXML
    ListView localFiles;

    @FXML
    ListView soulFiles;

    @FXML
    TextArea chatLog;

    @FXML
    ListView onlineUsers;

    private User user;

    public void initialize(URL location, ResourceBundle resources) {
        user = LoginController.getUser();
        System.out.println(user.getNickname());
        chatLog.appendText("Welcome in SoulFile program!");
        onlineUsers.getItems().addAll("Test", "Test2", "Test3", "Test4"); //Todo: Online users list
        Network.start();
        Thread t = new Thread(() -> {
            try {
                while(true){
                    AbstractMessage am = Network.readObject();
                    if (am instanceof FileMessage){
                        FileMessage fm = (FileMessage) am;
                        Files.write(Paths.get(user.getClientPath() + fm.getFilename()),fm.getData(), StandardOpenOption.CREATE);
                        refreshLocalFilesList();
                    }
                    if (am instanceof FileCommand){
                        refreshSoulFilesList();
                        System.out.println(((FileCommand) am).getMsg());
                    }
                }
            } catch (ClassNotFoundException | IOException e){
                e.printStackTrace();
            } finally {
                Network.stop();
            }
        });
        t.setDaemon(true);
        t.start();
        refreshLocalFilesList();
        refreshSoulFilesList();
    }

    public void refreshLocalFilesList() {
        updateUI(() -> {
            try {
                localFiles.getItems().clear();
                Files.list(Paths.get(user.getClientPath())).map(p -> p.getFileName().toString()).forEach(o -> localFiles.getItems().add(o));
                System.out.println("Local Files List Update");
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    public void refreshSoulFilesList() {
        updateUI(() -> {
            try {
                soulFiles.getItems().clear();
                Files.list(Paths.get(user.getServerPath())).map(p -> p.getFileName().toString()).forEach(o -> soulFiles.getItems().add(o));
                System.out.println("Soul Files List Update");
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    private void updateUI(Runnable r) {
        if (Platform.isFxApplicationThread()){
            r.run();
        } else {
            Platform.runLater(r);
        }
    }

    public void pressOnSoulDownloadBtn(ActionEvent actionEvent){
        if (takeSoulFile().length() > 0){
            Network.sendMsg(new FileRequest(takeSoulFile(),user));
            System.out.println("Send FileRequest: " + takeSoulFile());
        }
    }

    public void pressOnLocalUpdateBtn(ActionEvent actionEvent) throws IOException {
        if (takeLocalFile().length() > 0){
            Network.sendMsg(new FileMessage(Paths.get(user.getClientPath() + takeLocalFile()),user));
            System.out.println("Send FileMessage: " + Paths.get(user.getClientPath() + takeLocalFile()));
        }
    }

    private String takeLocalFile(){
        ObservableList lf = localFiles.getSelectionModel().getSelectedItems();
        String name = lf.get(0).toString();
        return name;
    }

    private String takeSoulFile() {
        ObservableList sf = soulFiles.getSelectionModel().getSelectedItems();
        String name = sf.get(0).toString();
        return name;
    }

    public void pressOnLocalDeleteBtn(ActionEvent actionEvent) throws IOException {
        if (takeLocalFile().length() > 0){
            Path path = Paths.get(user.getClientPath() + takeLocalFile());
            Files.delete(path);
            refreshLocalFilesList();
            System.out.println("Delete Local File: " + path);
        }
    }

    public void pressOnSoulDeleteBtn(ActionEvent actionEvent) throws IOException {
        if (takeSoulFile().length() > 0){
            Path path = Paths.get(user.getServerPath() + takeSoulFile());
            Files.delete(path);
            refreshSoulFilesList();
            System.out.println("Delete Soul File: " + path);
        }
    }
}
