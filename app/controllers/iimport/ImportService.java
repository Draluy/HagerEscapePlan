package controllers.iimport;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Stream;

import com.avaje.ebean.*;
import controllers.sum.ValueCounter;
import controllers.value.dao.ValueDAOJDBCBatchServiceImpl;
import controllers.value.dao.ValueConsumer;
import models.Sum;
import models.Value;
import play.Logger;
import play.db.DB;

/**
 * Created by dralu on 2/6/2016.
 * Gets the data to and from the DAO
 */
public class ImportService {

    private FileSystem filesystem = FileSystems.getDefault();
    private ValueDAOJDBCBatchServiceImpl valueDAOService = new ValueDAOJDBCBatchServiceImpl();
    private ValueCounter valueCounter = new ValueCounter();

    private static boolean stopImport = false;
    private static boolean importRunning = false;
    public static Path DATA_FILE = Paths.get("data/data.txt");

    /**
     * Import the file in the database
     *
     * @param offset The offset to start from. It is the line number to start from.
     * @return The Line number it stopped at
     */
    public long startImport(long offset) throws IOException {
        return startImport(offset, DATA_FILE);
    }

    /**
     * Will import all the lines of the specified file into the database
     * @param offset The offset to start the import from
     * @param file The file that has the data to import
     * @return The offset where the import ended
     * @throws IOException
     */
    public long startImport(long offset, Path file) throws IOException {

        if (importRunning) {
            return -1;
        }

        stopImport = false;
        importRunning = true;
        long currentOffset = 0L;

        //Open the file
        try (InputStream in = Files.newInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        ) {

            String line;

            //Catch up to the point of the file I should be
            for (int i = 0; i < offset; i++) {
                br.readLine();
            }

            //For each subsequent line, save into the database
            while ((line = br.readLine()) != null && !stopImport) {
                Value value = getValueFromString(line);
                //Save into the database
                valueDAOService.saveValue(value);
                //Aggregate the value for each country
                valueCounter.saveValue(value);

                currentOffset++;
                if (currentOffset % 10000 == 0) {
                    //Notify the front from time to time
                    ImportWebsocketActor.out.tell(String.valueOf(offset + currentOffset), ImportWebsocketActor.out);
                    valueDAOService.doPeriodically();
                    valueCounter.doPeriodically();
                }
            }
        } finally {
            importRunning = false;
            valueDAOService.doAtTheEnd();
            valueCounter.doAtTheEnd();
            ImportWebsocketActor.out.tell(String.valueOf(offset + currentOffset), ImportWebsocketActor.out);
        }
        return offset + currentOffset;
    }

    //Create a Value from a string read from the file
    //TODO Move this to a factory
    private Value getValueFromString(String line) {
        String[] tokens = line.split(",");
        Value value = new Value(Long.valueOf(tokens[0]),
                Long.valueOf(tokens[1]),
                tokens[2]);
        return value;
    }

    //Read the number of lines to import. Not very fast. Can be made faster using br.readLine() but not by much.
    public long getLineCount() {
        try (Stream<String> lines = Files.lines(DATA_FILE)) {
            return lines.count();
        } catch (IOException ex) {
            Logger.error("Could not read the number of lines to import", ex);
            return -1;
        }
    }


    /**
     * Pauses the import
     */
    public void pauseImport() {
        stopImport = true;
    }

    /**
     * Stops the import and empties the database.
     * Can be time consuming.
     */
    public void resetImport() {
        stopImport = true;
        try (Connection conn = DB.getConnection();
            Statement down = conn.createStatement();){
            down.execute("TRUNCATE TABLE VALUE");
            down.execute("TRUNCATE TABLE SUM");
        } catch (SQLException e) {
            Logger.error("Could not truncate a table", e);
        }
    }

    public void setFilesystem(FileSystem filesystem) {
        this.filesystem = filesystem;
    }

    public long getCurrentLineCount() {
        return valueDAOService.getCount();
    }

    public List<Sum> getValuesByCountry(){
        return valueCounter.getSumValues();
    }
}
