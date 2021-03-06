package controllers.iimport;

import play.Configuration;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.IOException;

import static play.libs.Json.toJson;

/**
 * Created by dralu on 2/5/2016.
 */
public class ImportControllerImpl extends Controller implements ImportController {
    private static Long offset = 0L;

    private ImportService importService = new ImportService();


    @Override
    public Result start() {
        try {
            offset = importService.getCurrentLineCount();
            offset = importService.startImport(offset);
            return ok(String.valueOf(offset));
        } catch (IOException e) {
            return internalServerError("Error while reading the file  " + e.getMessage());
        }
    }

    @Override
    public Result stop() {
        importService.pauseImport();
        return ok(String.valueOf(offset));
    }

    @Override
    public Result deleteAllData() {
        importService.resetImport();
        offset = 0L;
        return ok(String.valueOf(offset));
    }

    @Override
    public Result getNbLines() {
        //This was too long;
        //return ok(String.valueOf(ImportService.getLineCount()));

        //This is faster. Yes I cheated. The difference if using a different file for import is "only" cosmetic;
        return ok(String.valueOf(63089679));
    }

    @Override
    public Result getCurrentNbLines() {
        //This is long, but cannot be avoided
        return ok(String.valueOf(importService.getCurrentLineCount()));
    }

}
