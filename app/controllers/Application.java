package controllers;


import com.avaje.ebean.Model;
import controllers.iimport.ImportWebsocketActor;
import models.Value;
import play.data.Form;
import play.mvc.*;

import views.html.*;

import java.util.Arrays;
import java.util.List;

import static play.libs.Json.toJson;

/**
 * This is the entry point, this controller renders the index and holds the only websocket of the application
 */
public class Application extends Controller {

    public Result index() {
        return ok(index.render());
    }

    public  WebSocket<String> socket() {
        return WebSocket.withActor(ImportWebsocketActor::props);
    }

}
