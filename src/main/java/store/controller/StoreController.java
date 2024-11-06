package store.controller;

import java.util.List;
import store.domain.Products;
import store.domain.Promotions;
import store.global.constants.FilePath;
import store.global.file.ReadProductFile;
import store.global.file.ReadPromotionFile;
import store.global.file.dto.ProductFileDto;
import store.global.file.dto.PromotionFileDto;
import store.view.OutputView;

public class StoreController {
    private final ReadProductFile readProductFile;
    private final ReadPromotionFile readPromotionFile;
    private final OutputView outputView;

    public StoreController(
            ReadProductFile readProductFile,
            ReadPromotionFile readPromotionFile,
            OutputView outputView
    ) {
        this.readProductFile = readProductFile;
        this.readPromotionFile = readPromotionFile;
        this.outputView = outputView;
    }

    public void execute() {
        List<ProductFileDto> productDtos = readProductFile.getProductDtos(FilePath.PRODUCTS.getPath());
        Products products = new Products(productDtos);
        List<PromotionFileDto> promotionsDtos = readPromotionFile.getPromotionsDtos(FilePath.PROMOTIONS.getPath());
        Promotions promotions = new Promotions(promotionsDtos);

        outputView.printProducts(products);
    }
}
