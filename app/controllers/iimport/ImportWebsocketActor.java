package controllers.iimport;

import akka.actor.*;
import play.mvc.WebSocket;

/**
 * Handles the application websocket. Note that the out ActorRef is static to limit the possible out messages to one websocket.
 * @see UntypedActor for the rest.
 */
public class ImportWebsocketActor extends UntypedActor {

    //The websocket reference
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