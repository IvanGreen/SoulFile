package GreenCode.common;

public class FileCommand extends AbstractMessage {
    private String msg;

    public FileCommand(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
