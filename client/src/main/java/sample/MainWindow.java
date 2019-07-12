package sample;

import GreenCode.common.AbstractMessage;
import GreenCode.common.FileCommand;
import GreenCode.common.FileMessage;
import GreenCode.common.FileRequest;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

    public void initialize(URL location, ResourceBundle resources) {
        chatLog.appendText("Welcome in SoulFile program!");
        onlineUsers.getItems().addAll("Test", "Test2", "Test3", "Test4"); //Todo: Online users list
        Network.start();
        Thread t = new Thread(() -> {
            try {
                while(true){
                    AbstractMessage am = Network.readObject();
                    if (am instanceof FileMessage){
                        FileMessage fm = (FileMessage) am;
                        Files.write(Paths.get("common/src/main/resources/storage/clients/" + fm.getFilename()),fm.getData(), StandardOpenOption.CREATE);
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

    private void refreshLocalFilesList() {
        updateUI(() -> {
            try {
                localFiles.getItems().clear();
                Files.list(Paths.get("common/src/main/resources/storage/clients")).map(p -> p.getFileName().toString()).forEach(o -> localFiles.getItems().add(o));
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    private void refreshSoulFilesList() {
        updateUI(() -> {
            try {
                soulFiles.getItems().clear();
                Files.list(Paths.get("common/src/main/resources/storage/server")).map(p -> p.getFileName().toString()).forEach(o -> soulFiles.getItems().add(o));
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
            Network.sendMsg(new FileRequest(takeSoulFile()));
            System.out.println("Send FileRequest: " + takeSoulFile());
        }
    }

    public void pressOnLocalDownloadBtn(ActionEvent actionEvent) throws IOException {
        if (takeLocalFile().length() > 0){
            Network.sendMsg(new FileMessage(Paths.get("common/src/main/resources/storage/clients/" + takeLocalFile())));
            System.out.println("Send FileMessage: " + Paths.get("common/src/main/resources/storage/clients/" + takeLocalFile()));
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
}
