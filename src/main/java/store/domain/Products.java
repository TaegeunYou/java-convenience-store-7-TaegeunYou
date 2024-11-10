package store.domain;

import java.util.Collections;
import java.util.List;
import store.dto.BuyProductDto;
import store.global.constants.ErrorMessage;
import store.global.exception.CustomException;
import store.global.file.dto.ProductFileDto;

public class Products {
    List<Product> products;

    public Products(List<ProductFileDto> productFileDtos) {
        this.products = productFileDtos.stream().map(Product::new).toList();
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void validateBuyProduct(List<BuyProductDto> buyProducts) {
        for (BuyProductDto buyProduct : buyProducts) {
            Product matchedProduct = products.stream()
                    .filter(product -> product.isSame(buyProduct))
                    .findFirst()
                    .orElseThrow(() -> CustomException.of(ErrorMessage.PRODUCT_NOT_FOUND));
            matchedProduct.validateStock(buyProduct);
        }
    }
}
