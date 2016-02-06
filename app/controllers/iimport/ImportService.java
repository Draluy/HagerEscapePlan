package controllers.iimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import models.Value;
import play.Logger;

/**
 * Created by dralu on 2/6/2016.
 */
public class ImportService {

    private FileSystem filesystem = FileSystems.getDefault();

    private static Long offset = 0L;

    private static boolean stopImport = false;
    /**
     * Import the file in the database
     *
     * @param offset The offset to start from. It is the line number to start from.
     * @return The Line number it stopped at
     */
    public long startImport(long offset) throws IOException {
        File DATA_FILE = Paths.get("data/data.txt").toFile();
        this.offset = offset;

        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null && !stopImport ) {
                String[] tokens= line.split(",");
                Value value = new Value();
                value.timestamp = Long.valueOf(tokens[0]);
                value.value = Long.valueOf(tokens[1]);
                value.country = tokens[2];

                value.update();
                offset++;
            }
        }

        return offset;
    }


    public long pauseImport (){
        Long returnValue = offset;
        stopImport = true;
        return returnValue;
    }

    public void resetImport(){

    }

    public void setFilesystem(FileSystem filesystem) {
        this.filesystem = filesystem;
    }
}
