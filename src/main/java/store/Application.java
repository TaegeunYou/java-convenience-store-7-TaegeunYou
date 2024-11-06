package store;

import store.controller.StoreController;
import store.global.file.ReadProductFile;
import store.global.file.ReadPromotionFile;

public class Application {
    public static void main(String[] args) {
        StoreController storeController = new StoreController(
                new ReadProductFile(),
                new ReadPromotionFile()
        );
        storeController.execute();
    }
}
