package controllers.sum;

import play.mvc.Controller;
import play.mvc.Result;

import static play.libs.Json.toJson;

/**
 * Created by dralu on 2/10/2016.
 * Passes the request to the service. Laziest object in the whole project.
 */
public class SumValueController extends Controller {

    private ValueCounter valueCounter = new ValueCounter();

    public Result getValues(){
        return ok(toJson(valueCounter.getSumValues()));
    }

    public Result getSumTemplate(){
        return ok(views.html.sumValues.apply(valueCounter.getSumValues()));
    }

}
