package store.controller;

import java.util.List;
import store.dto.ProductFileDto;
import store.global.constants.FilePath;
import store.global.file.ReadProductFile;

public class StoreController {
    private final ReadProductFile readProductFile;
    public StoreController(ReadProductFile readProductFile) {
        this.readProductFile = readProductFile;
    }

    public void execute() {
        List<ProductFileDto> productDtos = readProductFile.getProductDtos(FilePath.PRODUCTS.getPath());
    }
}
