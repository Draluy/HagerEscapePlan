package controllers;

import controllers.iimport.ImportService;
import play.db.DB;
import play.mvc.Controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import play.mvc.Result;
import views.html.*;

/**
 * Created by dralu on 2/15/2016.
 */
public class TestsController extends Controller {

    public Result getTestResults (){
        Map<String, Boolean> testsMap = new HashMap<>();

        //1- Test the database connection
        try {
            testsMap.put( "Database connection", DB.getConnection().isValid(50));
        } catch (SQLException e) {
            testsMap.put( "Database connection not found due to "+ e.getMessage(), false);
        }

        //2- Test the data.txt presence
        testsMap.put("data.txt file found", ImportService.DATA_FILE.toFile().exists());
        return ok(tests.apply(testsMap));
    }
}
