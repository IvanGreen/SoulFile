package GreenCode.common;

import java.util.ArrayList;

public class SoulFile extends AbstractMessage {

    private ArrayList<String> arrayFilename;

    public SoulFile(ArrayList<String> arrayFilename) {
        this.arrayFilename = arrayFilename;
    }

    public ArrayList<String> getArrayFilename() {
        return arrayFilename;
    }
}
