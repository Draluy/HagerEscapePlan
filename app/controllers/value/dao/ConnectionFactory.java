package controllers.value.dao;

import play.db.DB;

import java.sql.Connection;

/**
 * Created by dralu on 2/12/2016.
 */
public class ConnectionFactory {
    public static Connection getConnection(){
        return DB.getConnection();
    }
}
