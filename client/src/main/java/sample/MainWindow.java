package sample;

import GreenCode.common.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {

    @FXML
    ListView<String> localFiles;

    @FXML
    ListView<String> soulFiles;

    private User user;

    public void initialize(URL location, ResourceBundle resources) {
        user = LoginController.getUser();
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
                        Log4j.log.info(((FileCommand) am).getMsg());
                    }
                    if (am instanceof SoulFile){
                        SoulFile sf = (SoulFile) am;
                        for (String o: sf.getArrayListFilename()) {
                            updateUI(() -> {
                                soulFiles.getItems().add(o);
                                Log4j.log.info("Soul Files List Update");
                            });
                        }
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
                Log4j.log.info("Local Files List Update User: " + user.getNickname());
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    public void refreshSoulFilesList() {
        updateUI(() -> {
            soulFiles.getItems().clear();
            Network.sendMsg(new SoulFileRequest(user));
            Log4j.log.info("Send SoulFileRequest: " + user.getNickname());
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
            Log4j.log.info("Send FileRequest: " + takeSoulFile());
        }
    }

    public void pressOnLocalUpdateBtn(ActionEvent actionEvent) throws IOException {
        if (takeLocalFile().length() > 0){
            Network.sendMsg(new FileMessage(Paths.get(user.getClientPath() + takeLocalFile()),user));
            Log4j.log.info("Send FileMessage: " + Paths.get(user.getClientPath() + takeLocalFile()));
        }
    }

    private String takeLocalFile(){
        ObservableList<String> lf = localFiles.getSelectionModel().getSelectedItems();
        return lf.get(0);
    }

    private String takeSoulFile() {
        ObservableList<String> sf = soulFiles.getSelectionModel().getSelectedItems();
        return sf.get(0);
    }

    public void pressOnLocalDeleteBtn(ActionEvent actionEvent) throws IOException {
        if (takeLocalFile().length() > 0){
            Path path = Paths.get(user.getClientPath() + takeLocalFile());
            Files.delete(path);
            refreshLocalFilesList();
            Log4j.log.info("Delete Local File: " + path);
        }
    }

    public void pressOnSoulDeleteBtn(ActionEvent actionEvent) throws IOException {
        if (takeSoulFile().length() > 0){
            Path path = Paths.get(user.getServerPath() + takeSoulFile());
            Files.delete(path);
            refreshSoulFilesList();
            Log4j.log.info("Delete Soul File: " + path);
        }
    }
}
