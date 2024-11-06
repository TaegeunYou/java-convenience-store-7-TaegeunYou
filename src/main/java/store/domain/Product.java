package store.domain;

import store.global.constants.ErrorMessage;
import store.global.exception.CustomException;
import store.global.file.dto.ProductFileDto;

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
        validateEmpty(productFileDto.name());
        validateNumber(productFileDto.price());
        validateNumber(productFileDto.quantity());
        validateEmpty(productFileDto.promotion());
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

    public String getPromotion() {
        return promotion;
    }
}
