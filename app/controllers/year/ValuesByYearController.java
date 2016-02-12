package controllers.year;

import play.mvc.Controller;
import play.mvc.Result;

import static play.libs.Json.toJson;

/**
 * Created by dralu on 2/11/2016.
 */
public class ValuesByYearController extends Controller {

    ValuesByYearService valuesByYearService = new ValuesByYearService();

    public Result getYears(){
        return ok(toJson(valuesByYearService.getYears()));
    }

    public Result getValues(int year){
        return ok(toJson(valuesByYearService.getValues(year).values()));
    }
}
