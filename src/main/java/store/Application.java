package store;

import store.controller.StoreController;
import store.global.file.ReadProductFile;

public class Application {
    public static void main(String[] args) {
        StoreController storeController = new StoreController(
                new ReadProductFile()
        );
        storeController.execute();
    }
}
