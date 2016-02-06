package java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.jimfs.Jimfs;
import controllers.iimport.ImportService;
import org.junit.*;

import org.mockito.MockitoAnnotations;
import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

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

    @Before
    public void before (){
        MockitoAnnotations.initMocks(this);
        importService = new ImportService();
    }

    @Test
    public void simpleCheck() throws IOException {
        importService.setFilesystem(Jimfs.newFileSystem());
        importService.startImport(0);
    }

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render();
        assertEquals("text/html", contentType(html));
        assertTrue(contentAsString(html).contains("Your new application is ready."));
    }


}
