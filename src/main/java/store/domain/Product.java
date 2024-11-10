package store.domain;

import store.dto.BuyProductDto;
import store.global.constants.ErrorMessage;
import store.global.exception.CustomException;
import store.global.file.dto.ProductFileDto;

public class Product {
    private final String name;
    private final int price;
    private final int quantity;
    private final Promotion promotion;

    public Product(ProductFileDto productFileDto) {
        this(productFileDto, null);
    }

    public Product(ProductFileDto productFileDto, Promotion promotion) {
        validate(productFileDto);
        this.name = productFileDto.name();
        this.price = Integer.parseInt(productFileDto.price());
        this.quantity = Integer.parseInt(productFileDto.quantity());
        this.promotion = promotion;
    }

    public boolean isSame(BuyProductDto buyProducts) {
        return name.equals(buyProducts.name());
    }

    public void validateStock(BuyProductDto buyProducts) {
        if (this.quantity < buyProducts.quantity()) {
            throw CustomException.of(ErrorMessage.STOCK_LIMIT_EXCEEDED);
        }
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

    public int getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
