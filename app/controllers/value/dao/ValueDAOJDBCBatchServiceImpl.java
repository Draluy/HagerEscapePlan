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

    @Override
    public void saveValue(Value val) {
        if(val != null)
        try {
            psInsert.setLong(1, val.timestamp);
            psInsert.setLong(2, val.value);
            psInsert.setString(3, val.country);
            psInsert.addBatch();
        } catch (SQLException e) {
            Logger.error("Eroor during adding of value "+val, e);
        }
    }

    @Override
    public void doPeriodically() {
        try {
            psInsert.executeBatch();
        } catch (SQLException e) {
            Logger.error("Eroor during execution of batch", e);
        }
    }

    @Override
    public void doAtTheEnd() {
        doPeriodically();
    }

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

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
