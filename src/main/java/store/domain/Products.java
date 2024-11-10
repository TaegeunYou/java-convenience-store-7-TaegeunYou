package store.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import store.dto.BuyProductDto;
import store.global.constants.ErrorMessage;
import store.global.exception.CustomException;
import store.global.file.dto.ProductFileDto;

public class Products {
    List<Product> products;

    public Products(List<ProductFileDto> productFileDtos, Promotions promotions) {
        Map<String, List<ProductFileDto>> productDtoMap = new LinkedHashMap<>();
        for (ProductFileDto dto : productFileDtos) {
            productDtoMap.computeIfAbsent(dto.name(), name -> new ArrayList<>()).add(dto);
        }
        this.products = generateProducts(productDtoMap, promotions);
    }

    private List<Product> generateProducts(Map<String, List<ProductFileDto>> productDtoMap, Promotions promotions) {
        ArrayList<Product> products = new ArrayList<>();
        for (Map.Entry<String, List<ProductFileDto>> entry : productDtoMap.entrySet()) {
            List<ProductFileDto> productDtos = entry.getValue();
            ProductFileDto normalProductDto = findNormalProductDto(productDtos);
            ProductFileDto promotionProductDto = findPromotionProductDto(productDtos);
            products.add(generateProduct(normalProductDto, promotionProductDto, promotions));
        }
        return products;
    }

    private Product generateProduct(ProductFileDto normalProductDto, ProductFileDto promotionProductDto,
                                    Promotions promotions) {
        if (promotionProductDto == null) {
            return new Product(normalProductDto);
        }
        return generateProductContainPromotion(normalProductDto, promotionProductDto, promotions);
    }

    private Product generateProductContainPromotion(ProductFileDto normalProductDto, ProductFileDto promotionProductDto,
                                                    Promotions promotions) {
        Promotion promotion = promotions.findPromotionByName(promotionProductDto.promotion());
        if (normalProductDto == null) {
            return new Product(promotionProductDto, promotion);
        }
        return new Product(normalProductDto, promotionProductDto, promotion);
    }

    private ProductFileDto findNormalProductDto(List<ProductFileDto> productDtos) {
        return productDtos.stream()
                .filter(dto -> dto.promotion() == null)
                .findFirst()
                .orElse(null);
    }

    private ProductFileDto findPromotionProductDto(List<ProductFileDto> productDtos) {
        return productDtos.stream()
                .filter(dto -> dto.promotion() != null)
                .findFirst()
                .orElse(null);
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
        int stock = matchedProducts.stream().mapToInt(Product::getNormalQuantity).sum();
        if (stock < buyProduct.quantity()) {
            throw CustomException.of(ErrorMessage.STOCK_LIMIT_EXCEEDED);
        }
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }
}
