package GreenCode.common;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class User implements Serializable {
    private String nickname;
    private String clientPath;
    private String serverPath;

    public User(String nickname) {
        this.nickname = nickname;
        this.clientPath = "common/src/main/resources/storage/clients/" + getNickname() + "/";
        this.serverPath = "common/src/main/resources/storage/server/" + getNickname() + "/";
        checkFolders();
        System.out.println("Add new User: " + getNickname() + "\n server folder: " + getServerPath() + "\n private folder: " + getClientPath());
    }

    public String getNickname() {
        return nickname;
    }

    public String getClientPath() {
        return clientPath;
    }

    public String getServerPath() {
        return serverPath;
    }

    private void checkFolders(){
        if (!Files.exists(Paths.get(getClientPath()))){
            Path path = Paths.get(getClientPath());
            try {
                Path newDir = Files.createDirectory(path);
                System.out.println("Created a new client folder for: " + getNickname());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!Files.exists(Paths.get(getServerPath()))){
            Path path = Paths.get(getServerPath());
            try {
                Path newDir = Files.createDirectory(path);
                System.out.println("Created a new server folder for: " + getNickname());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
