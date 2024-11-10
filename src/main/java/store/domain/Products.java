package store.domain;

import camp.nextstep.edu.missionutils.Console;
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

    public void buyProducts(List<BuyProductDto> buyProducts) {
        for (BuyProductDto buyProductDto : buyProducts) {
            Product product = findProduct(buyProductDto);
            if (product.hasActivePromotion()) {
                //프로모션 재고부터 소모하고 일반 재고에서 소모
                Promotion promotion = product.getPromotion();

                //프로모션_재고에서_가져올거
                int getProductForPromotionStock = Math.min(buyProductDto.quantity(), product.getPromotionQuantity());

                //프로모션_재고중에_공짜인거
                int getForFree = getProductForPromotionStock / (promotion.getBuy() + promotion.getGet());

                //차감하고_남은_프로모션_재고
                int remainPromotionStock = product.getPromotionQuantity() - getProductForPromotionStock;

                //프로모션_재고에서_해택_받고_가져온거
                int promotionBenefitQuantity = getForFree * (promotion.getBuy() + promotion.getGet());

                //일반_재고에서_가져올거
                int getProductForNormalStock = buyProductDto.quantity() - getProductForPromotionStock;

                if (getProductForPromotionStock - promotionBenefitQuantity >= promotion.getBuy() && remainPromotionStock >= promotion.getGet()) {
                    System.out.println("해택 받아서 더 가져올거야?");
                    if (Console.readLine().equals("Y")) {
                        getProductForPromotionStock += promotion.getGet();
                        promotionBenefitQuantity += promotion.getGet();
                    }
                }

                if (promotionBenefitQuantity != buyProductDto.quantity()) {
                    System.out.println("해택 안받은거 있는데 그래도 구매할거야?");
                    if (Console.readLine().equals("N")) {
                        product.minusPromotionStock(promotionBenefitQuantity);
                        continue;
                    }
                }
                product.minusPromotionStock(getProductForPromotionStock);
                product.minusNormalStock(getProductForNormalStock);
                continue;
            }
            int getProductForNormalStock = Math.min(buyProductDto.quantity(), product.getNormalQuantity());
            int getProductForPromotionStock = buyProductDto.quantity() - getProductForNormalStock;
            product.minusNormalStock(getProductForNormalStock);
            product.minusPromotionStock(getProductForPromotionStock);
        }
    }

    private Product findProduct(BuyProductDto buyProductDto) {
        return products.stream()
                .filter(it -> it.getName().equals(buyProductDto.name()))
                .findFirst()
                .orElseThrow(() -> CustomException.of(ErrorMessage.NOT_FOUND_PRODUCT));
    }
}
