package controllers.iimport;

import akka.actor.*;
import play.mvc.WebSocket;

public class ImportWebsocketActor extends UntypedActor {

    public static ActorRef out;

    public static Props props(ActorRef out) {
        return Props.create (ImportWebsocketActor.class, out);
    }

    public ImportWebsocketActor(ActorRef out) {
        this.out = out;
    }

    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            out.tell("I received your message: " + message, self());
        }
    }

    public void send(String msg) {
        out.tell(msg, self());
    }

}