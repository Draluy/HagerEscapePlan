package controllers.value.dao;

import models.Value;
import play.Logger;
import play.db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by dralu on 2/10/2016.
 */
public class ValueDAOJDBCServiceImpl implements ValueConsumer {

    private Connection connection = DB.getConnection();
    private PreparedStatement psInsert;

    public ValueDAOJDBCServiceImpl() {
        try {
            psInsert = connection.prepareStatement("insert into value(timestamp, value, country) values( ?,?,?)");
        } catch (SQLException e) {
            Logger.error("Method called on a closed conmnection.");
        }
    }

    @Override
    public void saveValue(Value val) {

        try {
            psInsert.setLong(1, val.timestamp);
            psInsert.setLong(2, val.value);
            psInsert.setString(3, val.country);
            psInsert.execute();
        } catch (SQLException e) {
            Logger.error("Eroor during adding of value "+val, e);
        }
    }

    @Override
    public void doPeriodically() {

    }

    @Override
    public void doAtTheEnd() {
        doPeriodically();
    }
}
