package GreenCode.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends AbstractMessage {
    private String filename;
    private byte[] data;
    private User owner;

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

    public FileMessage(Path path, User user) throws IOException {
        filename = path.getFileName().toString();
        data = Files.readAllBytes(path);
        owner = user;
    }

    public User getOwner() {
        return owner;
    }
}