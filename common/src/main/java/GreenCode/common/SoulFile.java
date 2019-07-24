package GreenCode.common;

import java.util.ArrayList;

public class SoulFile extends AbstractMessage {

    private ArrayList<String> arrayListFilenames;

    public SoulFile(ArrayList<String> arrayListFilenames) {
        this.arrayListFilenames = arrayListFilenames;
    }

    public ArrayList<String> getArrayListFilename() {
        return arrayListFilenames;
    }
}
