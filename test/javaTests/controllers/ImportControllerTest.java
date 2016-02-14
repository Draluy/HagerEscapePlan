package javaTests.controllers;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import com.google.common.collect.ImmutableMap;
import com.google.common.jimfs.Jimfs;
import controllers.iimport.ImportController;
import controllers.iimport.ImportControllerImpl;
import controllers.iimport.ImportService;
import controllers.iimport.ImportWebsocketActor;
import controllers.routes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import play.api.Application;
import play.core.j.JavaResultExtractor;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import play.test.FakeApplication;
import play.test.Helpers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.route;
import static play.test.Helpers.running;

/**
 * Created by dralu on 2/14/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ImportControllerTest extends WithApplication{

    @Mock
    private ImportService importService;

    private ImportControllerImpl importController;
    private Database database;


    @Override
    protected FakeApplication provideFakeApplication() {
        return new FakeApplication(new java.io.File("."), Helpers.class.getClassLoader(),
               new HashMap<String, Object>(), new ArrayList<String>(), null);
    }


    @Before
    public void before() throws SQLException {
        importController = new ImportControllerImpl();

        Whitebox.setInternalState(importController, "importService", importService);
    }

    @After
    public void after(){
    }
    
    @Test
    public void testStart () throws InterruptedException, IOException {
        importController.start();

        verify(importService).getCurrentLineCount();
        verify(importService).startImport(any(Long.class));
    }

    @Test
    public void testStop () throws InterruptedException, IOException {
        importController.stop();

        verify(importService).pauseImport();
    }

    @Test
    public void testdeleteAllData () throws InterruptedException, IOException {
        importController.deleteAllData();

        verify(importService).resetImport();
    }

    @Test
    public void testgetNbLines () throws InterruptedException, IOException {
        final Result nbLines = importController.getNbLines();

        assertEquals("63089679" , new String(JavaResultExtractor.getBody(nbLines,0L)));
    }

    @Test
    public void testgetCurrentNbLines () throws InterruptedException, IOException {
        doReturn(666L).when(importService).getCurrentLineCount();
        final Result nbLines = importController.getCurrentNbLines();

        assertEquals("666" , new String(JavaResultExtractor.getBody(nbLines,0L)));
    }

}
