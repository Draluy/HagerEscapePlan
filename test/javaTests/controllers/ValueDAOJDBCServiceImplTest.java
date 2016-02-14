package javaTests.controllers;

import com.avaje.ebean.Model;
import controllers.iimport.ImportControllerImpl;
import controllers.value.dao.ValueDAOJDBCServiceImpl;
import models.Value;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.test.FakeApplication;
import play.test.Helpers;
import play.test.WithApplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by dralu on 2/14/2016.
 */
public class ValueDAOJDBCServiceImplTest  extends WithApplication {
    private ValueDAOJDBCServiceImpl valueDAOJDBCService;

    private Database database;

    @Override
    protected FakeApplication provideFakeApplication() {
        return new FakeApplication(new java.io.File("."), Helpers.class.getClassLoader(),
                new HashMap<String, Object>(), new ArrayList<String>(), null);
    }

    @Before
    public void before() throws SQLException {
        database =Databases.inMemory();
        database.getConnection().setAutoCommit(true);
        Evolutions.applyEvolutions(database);
        valueDAOJDBCService = new ValueDAOJDBCServiceImpl();
        valueDAOJDBCService.setConnection(database.getConnection());
    }

    @After public void after(){
        Evolutions.cleanupEvolutions(database);
        database.shutdown();
    }

    @Test
    public void testSaveValueNull()  throws SQLException {
        valueDAOJDBCService.saveValue(null);

        ResultSet resultSet = database.getConnection().createStatement().executeQuery("select * from value");

        assertFalse(resultSet.next());
    }

    @Test
    public void testSaveValue() throws SQLException {
        Value value = new Value();
        value.country="France";
        value.timestamp = 1L;
        value.value = 2L;
        valueDAOJDBCService.saveValue(value);

        ResultSet resultSet = database.getConnection().createStatement().executeQuery("select * from value");

        assertTrue(resultSet.next());
    }
}
