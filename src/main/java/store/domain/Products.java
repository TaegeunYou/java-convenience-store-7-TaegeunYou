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
        int stock = matchedProducts.stream().mapToInt(product ->
                product.getNormalQuantity() + product.getPromotionQuantity()
        ).sum();
        if (stock < buyProduct.quantity()) {
            throw CustomException.of(ErrorMessage.STOCK_LIMIT_EXCEEDED);
        }
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public BuyProductsResult buyProducts(List<BuyProductDto> buyProductDtos) {
        List<BuyProductResult> buyProductResults = new ArrayList<>();
        for (BuyProductDto buyProductDto : buyProductDtos) {
            Product product = findProduct(buyProductDto);
            if (product.hasActivePromotion()) {
                int getProductForPromotionStock = Math.min(buyProductDto.quantity(), product.getPromotionQuantity());
                BuyProductResult buyProductResult = new BuyProductResult(
                        product.getPromotion(), getProductForPromotionStock,
                        buyProductDto.quantity(), product);
                buyProductResults.add(buyProductResult);
                continue;
            }
            BuyProductResult buyProductResult = new BuyProductResult(
                    product.getPromotion(), 0, buyProductDto.quantity(), product
            );
            buyProductResults.add(buyProductResult);
        }
        return new BuyProductsResult(buyProductResults);
    }

    private Product findProduct(BuyProductDto buyProductDto) {
        return products.stream()
                .filter(it -> it.getName().equals(buyProductDto.name()))
                .findFirst()
                .orElseThrow(() -> CustomException.of(ErrorMessage.NOT_FOUND_PRODUCT));
    }

    public void applyPurchaseReduction(BuyProductsResult buyProductsResult) {
        for (BuyProductResult buyProductResult : buyProductsResult.getBuyProducts()) {
            Product product = buyProductResult.getProduct();
            if (product.hasActivePromotion()) {
                product.minusPromotionStock(buyProductResult.getProductForPromotionStock());
                product.minusNormalStock(buyProductResult.getProductForNormalStock());
                continue;
            }
            int getProductForNormalStock = Math.min(buyProductResult.getTotalQuantity(), product.getNormalQuantity());
            int getProductForPromotionStock = buyProductResult.getTotalQuantity() - getProductForNormalStock;
            product.minusNormalStock(getProductForNormalStock);
            product.minusPromotionStock(getProductForPromotionStock);
        }
    }
}
