package javaTests;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import akka.actor.*;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;
import com.google.common.jimfs.Jimfs;
import com.typesafe.config.ConfigFactory;
import controllers.iimport.ImportService;
import controllers.iimport.ImportWebsocketActor;
import org.junit.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import play.Application;
import play.db.DB;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.twirl.api.Content;

import static play.inject.Bindings.bind;
import static play.test.Helpers.*;
import static org.junit.Assert.*;

/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ImportServiceTest {

    private ImportService importService;


    static class MyActor extends UntypedActor {
        public void onReceive(Object o) throws Exception {
            if (o.equals("say42")) {
                getSender().tell(42, getSelf());
            } else if (o instanceof Exception) {
                throw (Exception) o;
            }
        }
        public boolean testMe() { return true; }
    }

    @Test
    public void demonstrateTestActorRef() {


        final TestActorRef<MyActor> ref = TestActorRef.create(ActorSystem.create(), Props.create(MyActor.class), "testA");
        final MyActor actor = ref.underlyingActor();
        assertTrue(actor.testMe());
    }



    @Before
    public void before () {
        MockitoAnnotations.initMocks(this);
        importService = new ImportService();
        ImportWebsocketActor.out = TestActorRef.create(ActorSystem.create(), Props.create(MyActor.class), "testA");
    }


    @After
    public void shutdownDatabase() throws SQLException {

    }

    @Test
    public void testImport() throws IOException, SQLException {
        Database database = Databases.inMemory("default");
        database.getConnection().setAutoCommit(true);
        Evolutions.applyEvolutions(database);

        Application application = new GuiceApplicationBuilder()
                .overrides(bind(Database.class).toInstance(database)
                ).build();
        running(application, new Runnable() {
            public void run() {
                FileSystem filesystem = Jimfs.newFileSystem();
                importService.setFilesystem(filesystem);
                final Path data = filesystem.getPath("data", "data.txt");
                try {
                    Files.createDirectory(filesystem.getPath("data"));

                Files.createFile(data);
                Files.write(data, Arrays.asList("1355356089632,14,Egypte",
                        "1355356092626,77,Autriche",
                        "1355356096484,7,Tunisie",
                        "1355356097541,37,Autriche",
                        "1355356099591,75,Pays-Bas",
                        "1355356102596,93,Etats-Unis",
                        "1355356103729,66,Espagne",
                        "1355356103966,94,Irlande",
                        "1355356105432,22,France",
                        "1355356107731,61,Espagne",
                        "1355356109046,78,Maroc",
                        "1355356109179,97,Pakistan",
                        "1355356110031,60,Tunisie",
                        "1355356111280,58,Autriche",
                        "1355356111431,35,Pays-Bas",
                        "1355356113312,55,Egypte",
                        "1355356116525,98,Espagne",
                        "1355356117918,22,Iran",
                        "1355356118327,2,Portugal",
                        "1355356120558,11,Corée du Sud",
                        "1355356121516,87,Tunisie",
                        "1355356123757,21,Maroc",
                        "1355356126876,8,Brésil",
                        "1355356129505,56,Irlande",
                        "1355356129585,12,Pérou",
                        "1355356130274,61,Suisse",
                        "1355356134106,12,Suisse"), StandardCharsets.UTF_8);

                importService.startImport(0, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Assert.assertEquals(27, ImportService.getCurrentLineCount());

            }
        });
        Evolutions.cleanupEvolutions(database);
        database.shutdown();
    }

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render();
        assertEquals("text/html", contentType(html));
        assertTrue(contentAsString(html).contains("Your new application is ready."));
    }


}
