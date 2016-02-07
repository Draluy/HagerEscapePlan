package controllers.iimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

import com.avaje.ebean.*;
import com.avaje.ebean.config.PersistBatch;
import models.Value;
import play.Logger;

import javax.persistence.OptimisticLockException;

/**
 * Created by dralu on 2/6/2016.
 */
public class ImportService {

    private FileSystem filesystem = FileSystems.getDefault();


    private static boolean stopImport = false;
    private static boolean importRunning = false;

    private static File DATA_FILE = Paths.get("data/data.txt").toFile();

    /**
     * Import the file in the database
     *
     * @param offset The offset to start from. It is the line number to start from.
     * @return The Line number it stopped at
     */
    public long startImport(final long offset) throws IOException {

        if (importRunning){
            return -1;
        }

        stopImport = false;
        importRunning = true;
        long currentOffset = 0L;

        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;

            Transaction transaction = getTransaction();
            while ((line = br.readLine()) != null && !stopImport) {
                Value value = getValueFromString(line);
                value.save();

                currentOffset++;
                if (currentOffset % 10000 == 0) {
                    //Notify the front
                    ImportWebsocketActor.out.tell(String.valueOf(offset +currentOffset), ImportWebsocketActor.out);

                    //Commit periodically, or else the commits take too much time and pausing the process takes too long
                    transaction.commit();
                    transaction = getTransaction();
                }
            }
            transaction.commit();
        }
        finally {

            importRunning = false;
        }
        return offset + currentOffset;
    }

    private Transaction getTransaction() {
        Transaction transaction  = Value.db().beginTransaction();
        transaction.setBatchMode(true);
        transaction.setBatchSize(1000);
        transaction.setBatch(PersistBatch.ALL);
        return transaction;
    }

    private Value getValueFromString(String line) {
        String[] tokens = line.split(",");
        Value value = new Value();
        value.timestamp = Long.valueOf(tokens[0]);
        value.value = Long.valueOf(tokens[1]);
        value.country = tokens[2];
        return value;
    }

    public static long getLineCount() {
        try (Stream<String> lines = Files.lines(DATA_FILE.toPath())) {
            return lines.count();
        } catch (IOException ex) {
            Logger.error("Could not read the number of lines to import", ex);
            return -1;
        }
    }


    public void pauseImport() {
        stopImport = true;
    }

    public void resetImport() {
        stopImport = true;
        SqlUpdate down = Ebean.createSqlUpdate("TRUNCATE TABLE VALUE");
        down.execute();
    }

    public void setFilesystem(FileSystem filesystem) {
        this.filesystem = filesystem;
    }

    public static long getCurrentLineCount() {
        return new Model.Finder(Value.class).findRowCount();
    }
}
