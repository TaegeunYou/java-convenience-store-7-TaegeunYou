package store.domain;

import store.dto.BuyProductDto;
import store.global.constants.ErrorMessage;
import store.global.exception.CustomException;
import store.global.file.dto.ProductFileDto;

public class Product {
    private final String name;
    private final int price;
    private int normalQuantity;
    private final Promotion promotion;
    private int promotionQuantity;

    public Product(ProductFileDto normalProductDto) {
        validate(normalProductDto);
        this.name = normalProductDto.name();
        this.price = Integer.parseInt(normalProductDto.price());
        this.normalQuantity = Integer.parseInt(normalProductDto.quantity());
        this.promotion = null;
        this.promotionQuantity = 0;
    }

    public Product(ProductFileDto promotionProductDto, Promotion promotion) {
        validate(promotionProductDto);
        this.name = promotionProductDto.name();
        this.price = Integer.parseInt(promotionProductDto.price());
        this.normalQuantity = 0;
        this.promotion = promotion;
        this.promotionQuantity = Integer.parseInt(promotionProductDto.quantity());
    }

    public Product(ProductFileDto normalProductDto, ProductFileDto promotionProductDto, Promotion promotion) {
        validate(normalProductDto);
        validate(promotionProductDto);
        this.name = normalProductDto.name();
        this.price = Integer.parseInt(normalProductDto.price());
        this.normalQuantity = Integer.parseInt(normalProductDto.quantity());
        this.promotion = promotion;
        this.promotionQuantity = Integer.parseInt(promotionProductDto.quantity());
    }

    public boolean isSame(BuyProductDto buyProducts) {
        return name.equals(buyProducts.name());
    }

    private void validate(ProductFileDto productFileDto) {
        validateEmpty(productFileDto.name());
        validateNumber(productFileDto.price());
        validateNumber(productFileDto.quantity());
        validateBlank(productFileDto.promotion());
    }

    private void validateEmpty(String str) {
        if (isEmptyOrBlank(str)) {
            throw CustomException.of(ErrorMessage.EMPTY_NOT_ALLOWED);
        }
    }

    private void validateBlank(String str) {
        if (str == null) {
            return;
        }
        if (isBlank(str)) {
            throw CustomException.of(ErrorMessage.BLANK_NOT_ALLOWED);
        }
    }

    private void validateNumber(String str) {
        if (isNotPositiveInteger(str)) {
            throw CustomException.of(ErrorMessage.INVALID_NUMBER);
        }
    }

    private boolean isEmptyOrBlank(String str) {
        return str == null || str.isBlank();
    }

    private boolean isBlank(String str) {
        return str.isBlank();
    }

    private boolean isNotPositiveInteger(String str) {
        return !str.matches("\\d+") || Integer.parseInt(str) < 0;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getNormalQuantity() {
        return normalQuantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public int getPromotionQuantity() {
        return promotionQuantity;
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public boolean hasActivePromotion() {
        return hasPromotion() && promotion.isActive();
    }

    public void minusPromotionStock(int promotionQuantity) {
        this.promotionQuantity -= promotionQuantity;
    }

    public void minusNormalStock(int normalQuantity) {
        this.normalQuantity -= normalQuantity;
    }
}
