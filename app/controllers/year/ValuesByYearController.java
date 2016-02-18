package controllers.year;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static play.libs.Json.toJson;

/**
 * Created by dralu on 2/11/2016.
 */
public class ValuesByYearController extends Controller {

    ValuesByYearService valuesByYearService = new ValuesByYearService();

    //Gets all the possible years from the imported data
    public Result getYears(){
        return ok(toJson(valuesByYearService.getYears()));
    }

    /**
     * Gets all the values, aggregated by day, for a specific year
     * @param year The year
     */
    public Result getValues(int year){
        return ok(toJson(valuesByYearService.getValues(year).values()));
    }

    /**
     * Gets all the values for a specific period.
     * @return
     */
    public Result getValuesByPeriod(){
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String lowerDate = dynamicForm.get("lowerbound");
        String upperDate = dynamicForm.get("upperbound");
        LocalDateTime lowerBound = null;
        LocalDateTime upperBound = null;

        //Dates should arrive with a ISO-8601 compatible format.
        if (lowerDate != null && upperDate != null) {
             lowerBound = LocalDateTime.parse(lowerDate);
             upperBound = LocalDateTime.parse(upperDate);
        }

        //If the dates are valid, pass them to the service
        if (lowerBound != null && upperBound != null) {
            return ok(toJson(valuesByYearService.getValues(lowerBound, upperBound).values()));
        }else{
            return internalServerError("Bounds could not be parsed according to the ISO 8601 format.");
        }
    }
}
