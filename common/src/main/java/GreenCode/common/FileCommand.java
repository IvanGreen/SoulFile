package GreenCode.common;

public class FileCommand extends AbstractMessage {
    private String msg;

    public FileCommand(String msg, User user) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
