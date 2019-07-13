package GreenCode.common;


public class FileRequest extends AbstractMessage {
    private String filename;
    private User owner;

    public String getFilename(){ return filename;}

    public FileRequest(String filename,User user){
        this.filename = filename;
        this.owner = user;
    }

    public User getOwner() {
        return owner;
    }
}
