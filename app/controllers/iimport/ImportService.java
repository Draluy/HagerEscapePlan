package controllers.iimport;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;

import com.avaje.ebean.*;
import com.avaje.ebean.annotation.SqlSelect;
import com.avaje.ebean.config.PersistBatch;
import models.Value;
import play.Logger;
import play.db.DB;

import javax.persistence.OptimisticLockException;

/**
 * Created by dralu on 2/6/2016.
 */
public class ImportService {

    private FileSystem filesystem = FileSystems.getDefault();


    private static boolean stopImport = false;
    private static boolean importRunning = false;

    private static Path DATA_FILE = Paths.get("data/data.txt");

    /**
     * Import the file in the database
     *
     * @param offset The offset to start from. It is the line number to start from.
     * @return The Line number it stopped at
     */
/*    public long startImport(final long offset) throws IOException {

        if (importRunning){
            return -1;
        }

        stopImport = false;
        importRunning = true;
        long currentOffset = 0L;

        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;

            Ebean.beginTransaction();

            //Catch up to the point of the file I should be
            for (int i=0;i<offset;i++){
                br.readLine();
            }

            while ((line = br.readLine()) != null && !stopImport) {
                Value value = getValueFromString(line);
                value.save();

                currentOffset++;
                if (currentOffset % 10000 == 0) {
                    //Notify the front
                    ImportWebsocketActor.out.tell(String.valueOf(offset +currentOffset), ImportWebsocketActor.out);

                    //Commit periodically, or else the commits take too much time and pausing the process takes too long
                    Ebean.commitTransaction();
                    Ebean.beginTransaction();
                }
            }
            Ebean.commitTransaction();
        }
        finally {

            importRunning = false;
        }
        return offset + currentOffset;
    }*/

    public long startImport (long offset) throws IOException {
        return startImport(offset, DATA_FILE);
    }

    public long startImport (long offset, Path file) throws IOException {

        if (importRunning){
            return -1;
        }

        stopImport = false;
        importRunning = true;
        long currentOffset = 0L;

        try (InputStream in = Files.newInputStream(file); BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
             Connection connection = DB.getConnection();
             Statement statement = connection.createStatement();) {
            String line;

            //Catch up to the point of the file I should be
            for (int i=0;i<offset;i++){
                br.readLine();
            }
            PreparedStatement psInsert =  connection.prepareStatement("insert into value(timestamp, value, country) values( ?,?,?)");

            while ((line = br.readLine()) != null && !stopImport) {
                Value value = getValueFromString(line);

                psInsert.setLong (1,value.timestamp);
                psInsert.setLong (2,value.value);
                psInsert.setString (3,value.country);
                psInsert.execute();

                currentOffset++;
                if (currentOffset % 10000 == 0) {
                    //Notify the front
                    ImportWebsocketActor.out.tell(String.valueOf(offset +currentOffset), ImportWebsocketActor.out);

                    //Commit periodically, or else the commits take too much time and pausing the process takes too long
                    //statement.executeBatch();
                }
            }
        }catch(SQLException ex){
            Logger.error("Error while inserting value",ex);
        }
        finally {
            importRunning = false;
        }
        ImportWebsocketActor.out.tell(String.valueOf(offset +currentOffset), ImportWebsocketActor.out);
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

    public long getLineCount() {
        try (Stream<String> lines = Files.lines(DATA_FILE)) {
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
        SqlQuery down = Ebean.createSqlQuery("select count(*) as count from VALUE");
        return down.findList().get(0).getLong("count");
    }
}
