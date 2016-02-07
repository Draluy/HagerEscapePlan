package controllers.iimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlUpdate;
import models.Value;
import play.Logger;

import javax.persistence.OptimisticLockException;

/**
 * Created by dralu on 2/6/2016.
 */
public class ImportService {

    private FileSystem filesystem = FileSystems.getDefault();

    private static Long offset = 0L;

    private static boolean stopImport = false;

    private static File DATA_FILE = Paths.get("data/data.txt").toFile();

    /**
     * Import the file in the database
     *
     * @param offset The offset to start from. It is the line number to start from.
     * @return The Line number it stopped at
     */
    public long startImport(long offset) throws IOException {

        this.offset = offset;

        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null && !stopImport) {
                String[] tokens = line.split(",");
                Value value = new Value();
                value.timestamp = Long.valueOf(tokens[0]);
                value.value = Long.valueOf(tokens[1]);
                value.country = tokens[2];

                value.save();

                offset++;

                if (offset % 1000 == 0) {
                    ImportWebsocketActor.out.tell(offset, ImportWebsocketActor.out);
                }
            }
        }

        return offset;
    }

    public static long getLineCount() {
        try (Stream<String> lines = Files.lines(DATA_FILE.toPath())) {
            return lines.count();
        } catch (IOException ex) {
            Logger.error("Could not read the number of lines to import", ex);
            return -1;
        }
    }


    public long pauseImport() {
        stopImport = true;
        return offset;
    }

    public void resetImport() {
        SqlUpdate down = Ebean.createSqlUpdate("TRUNCATE TABLE VALUE");
        down.execute();
        offset = 0L;
    }

    public void setFilesystem(FileSystem filesystem) {
        this.filesystem = filesystem;
    }
}
