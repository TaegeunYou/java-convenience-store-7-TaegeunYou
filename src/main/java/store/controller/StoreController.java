package store.controller;

import store.global.constants.FilePath;
import store.global.file.ReadFile;

public class StoreController {
    public void execute() {
        ReadFile.getAllLines(FilePath.PRODUCTS.getPath());
    }
}
