package controllers.value.dao;

import models.Value;
import play.Logger;
import play.db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dralu on 2/10/2016.
 * Inserts values in the database using the batch jdbc API.
 * JPA Annotations are nice, but the ebean ORM is not fast and/or customizable enough for big inserts.
 */
public class ValueDAOJDBCBatchServiceImpl implements ValueConsumer {

    private Connection connection = DB.getConnection();
    private PreparedStatement psInsert;

    public ValueDAOJDBCBatchServiceImpl() {
        try {
            psInsert = connection.prepareStatement("insert into value(timestamp, value, country) values( ?,?,?)");
        } catch (SQLException e) {
            Logger.error("Method called on a closed conmnection.");
        }
    }

    /**
     * Saves a value in the database. Puts it in memory. Data is flushed when calling doPeriodically.
     * @param val The value to insert.
     */
    @Override
    public void saveValue(Value val) {
        if(val != null)
        try {
            psInsert.setLong(1, val.getTimestamp());
            psInsert.setLong(2, val.getValue());
            psInsert.setString(3, val.getCountry());
            psInsert.addBatch();
        } catch (SQLException e) {
            Logger.error("Eroor during adding of value "+val, e);
        }
    }

    /**
     * Flush the batched data.
     */
    @Override
    public void doPeriodically() {
        try {
            psInsert.executeBatch();
        } catch (SQLException e) {
            Logger.error("Eroor during execution of batch", e);
        }
    }

    /**
     * Flush the batched data. Name change so the calling code is more explicit.
     */
    @Override
    public void doAtTheEnd() {
        doPeriodically();
    }

    /**
     * Get the count of all rows in the table. This is very slow with pgsql.
     * @return The number of lines in the database.
     */
    public Long getCount(){
        try {
            final ResultSet resultSet = connection.createStatement().executeQuery("select count(*) as count from VALUE");
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            Logger.error("Could not retrieve the number of lines of a table.");
        }
        return -1L;
    }

    /**
     * Setter for the connection, for testability
     * @param connection
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
