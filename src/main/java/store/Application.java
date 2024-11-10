package store;

import store.controller.StoreController;
import store.global.file.ReadProductFile;
import store.global.file.ReadPromotionFile;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        StoreController storeController = new StoreController(
                new ReadProductFile(),
                new ReadPromotionFile(),
                new InputView(),
                new OutputView()
        );
        storeController.execute();
    }
}
