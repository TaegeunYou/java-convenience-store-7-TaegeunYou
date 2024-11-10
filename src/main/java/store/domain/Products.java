package store.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import store.dto.BuyProductDto;
import store.global.constants.ErrorMessage;
import store.global.exception.CustomException;
import store.global.file.dto.ProductFileDto;

public class Products {
    List<Product> products;

    public Products(List<ProductFileDto> productFileDtos, Promotions promotions) {
        List<Product> products = new ArrayList<>();
        for (ProductFileDto productDto : productFileDtos) {
            Promotion promotion = promotions.findPromotionByName(productDto.promotion());
            if (promotion != null) {
                products.add(new Product(productDto, promotion));
                continue;
            }
            products.add(new Product(productDto));
        }
        this.products = products;
    }

    public void validateBuyProduct(List<BuyProductDto> buyProducts) {
        for (BuyProductDto buyProduct : buyProducts) {
            List<Product> matchedProducts = products.stream()
                    .filter(product -> product.isSame(buyProduct)).toList();
            validateProductFound(matchedProducts);
            validateStock(matchedProducts, buyProduct);
        }
    }

    private void validateProductFound(List<Product> matchedProducts) {
        if (matchedProducts.isEmpty()) {
            throw CustomException.of(ErrorMessage.PRODUCT_NOT_FOUND);
        }
    }

    private void validateStock(List<Product> matchedProducts, BuyProductDto buyProduct) {
        int stock = matchedProducts.stream().mapToInt(Product::getQuantity).sum();
        if (stock < buyProduct.quantity()) {
            throw CustomException.of(ErrorMessage.STOCK_LIMIT_EXCEEDED);
        }
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }
}
