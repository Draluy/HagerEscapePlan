package controllers.value;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import models.Value;
import play.mvc.Controller;
import play.mvc.Result;

import static play.libs.Json.toJson;

/**
 * Created by dralu on 2/8/2016.
 */
public class ValueController extends Controller {

    public Result getValue(Long id) {
        Value value = new Model.Finder<Long, Value>(Value.class).byId(id);
        return ok(toJson(value));
    }


}
