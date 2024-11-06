package store.domain;

import store.global.constants.ErrorMessage;
import store.global.exception.CustomException;
import store.global.file.ProductFileDto;

public class Product {
    private final String name;
    private final int price;
    private final int quantity;
    private final String promotion;

    public Product(ProductFileDto productFileDto) {
        validate(productFileDto);
        this.name = productFileDto.name();
        this.price = Integer.parseInt(productFileDto.price());
        this.quantity = Integer.parseInt(productFileDto.quantity());
        this.promotion = productFileDto.promotion();
    }

    private void validate(ProductFileDto productFileDto) {
        validateName(productFileDto.name());
        validatePrice(productFileDto.price());
        validateQuantity(productFileDto.quantity());
        validatePromotion(productFileDto.promotion());
    }

    private void validateName(String name) {
        validateEmpty(name);
    }

    private void validatePrice(String price) {
        validateEmpty(price);
        validateNumber(price);
    }

    private void validateQuantity(String quantity) {
        validateEmpty(quantity);
        validateNumber(quantity);
    }

    private void validatePromotion(String promotion) {
        validateEmpty(promotion);
    }

    private void validateEmpty(String str) {
        if (isEmptyOrBlank(str)) {
            throw CustomException.of(ErrorMessage.EMPTY_NOT_ALLOWED);
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

    private boolean isNotPositiveInteger(String str) {
        return !str.matches("\\d+") || Integer.parseInt(str) <= 0;
    }
}
