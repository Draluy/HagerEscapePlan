package controllers.sum;

import play.mvc.Controller;
import play.mvc.Result;

import static play.libs.Json.toJson;

/**
 * Created by dralu on 2/10/2016.
 */
public class SumValueController extends Controller {

    public Result getValues(){
        return ok(toJson(ValueCounter.getSumValues()));
    }

    public Result getSumTemplate(){
        return ok(views.html.sumValues.apply(ValueCounter.getSumValues()));
    }

}
