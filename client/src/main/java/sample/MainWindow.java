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
    ListView localFiles;

    @FXML
    ListView soulFiles;

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
                        System.out.println(((FileCommand) am).getMsg());
                    }
                    if (am instanceof SoulFile){
                        SoulFile sf = (SoulFile) am;
                        for (String o: sf.getArrayListFilename()) {
                            updateUI(() -> {
                                soulFiles.getItems().add(o);
                                System.out.println("Soul Files List Update");
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
                System.out.println("Local Files List Update");
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    public void refreshSoulFilesList() {
        updateUI(() -> {
            soulFiles.getItems().clear();
            Network.sendMsg(new SoulFileRequest(user));
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
        return lf.get(0).toString();
    }

    private String takeSoulFile() {
        ObservableList sf = soulFiles.getSelectionModel().getSelectedItems();
        return sf.get(0).toString();
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
