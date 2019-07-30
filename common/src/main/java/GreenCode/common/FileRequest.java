package GreenCode.common;


public class FileRequest extends AbstractMessage {
    private String filename;

    public String getFilename(){ return filename;}

    public FileRequest(String filename,User user){
        super(user);
        this.filename = filename;
    }

}
