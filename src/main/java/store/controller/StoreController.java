package store.controller;

import java.util.List;
import store.domain.Products;
import store.global.file.ProductFileDto;
import store.global.constants.FilePath;
import store.global.file.PromotionFileDto;
import store.global.file.ReadProductFile;
import store.global.file.ReadPromotionFile;

public class StoreController {
    private final ReadProductFile readProductFile;
    private final ReadPromotionFile readPromotionFile;

    public StoreController(ReadProductFile readProductFile, ReadPromotionFile readPromotionFile) {
        this.readProductFile = readProductFile;
        this.readPromotionFile = readPromotionFile;
    }

    public void execute() {
        List<ProductFileDto> productDtos = readProductFile.getProductDtos(FilePath.PRODUCTS.getPath());
        Products products = new Products(productDtos);
        List<PromotionFileDto> promotionsDtos = readPromotionFile.getPromotionsDtos(FilePath.PROMOTIONS.getPath());
    }
}
