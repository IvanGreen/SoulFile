package GreenCode.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends AbstractMessage {
    private String filename;
    private byte[] data;

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

    public FileMessage(Path path, User user) throws IOException {
        super(user);
        filename = path.getFileName().toString();
        data = Files.readAllBytes(path);
    }

}