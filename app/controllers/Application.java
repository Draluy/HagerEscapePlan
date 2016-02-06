package controllers;


import com.avaje.ebean.Model;
import controllers.iimport.ImportWebsocketActor;
import models.Value;
import play.data.Form;
import play.mvc.*;

import views.html.*;

import java.util.List;

import static play.libs.Json.toJson;

public class Application extends Controller {

    public Result index() {
        return ok(index.render());
    }

    public Result addValue(){
        Value value = Form.form(Value.class).bindFromRequest().get();
        value.save();
        return redirect(routes.Application.index());
    }

    public Result getValues(){
        List<Value> values = new Model.Finder(Value.class).all();
        return ok(toJson(values));
    }

    public WebSocket<String> socket() {
        return WebSocket.withActor(ImportWebsocketActor::props);
    }
}
