package controllers.iimport;

import play.mvc.Controller;
import play.mvc.Result;

import java.io.IOException;

/**
 * Created by dralu on 2/5/2016.
 */
public class ImportControllerImpl extends Controller implements ImportController {

    private final ImportService importService = new ImportService();

    @Override
    public Result start(long start) {
        try {
            importService.startImport(start);
            return ok(String.valueOf(start));
        } catch (IOException e) {
            return internalServerError(e.getMessage());
        }
    }

    @Override
    public Result stop() {
        return ok(String.valueOf(importService.pauseImport()));
    }

    @Override
    public Result deleteAllData() {
        importService.resetImport();
        return ok();
    }

    @Override
    public Result getNbLines() {
        return ok(String.valueOf(ImportService.getLineCount()));
    }


}