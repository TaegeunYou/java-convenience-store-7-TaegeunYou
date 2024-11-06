package store.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import store.global.constants.ErrorMessage;
import store.global.exception.CustomException;
import store.global.file.PromotionFileDto;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final String startDate;
    private final String endDate;

    public Promotion(PromotionFileDto promotionFileDto) {
        validate(promotionFileDto);
        this.name = promotionFileDto.name();
        this.buy = Integer.parseInt(promotionFileDto.buy());
        this.get = Integer.parseInt(promotionFileDto.get());
        this.startDate = promotionFileDto.startDate();
        this.endDate = promotionFileDto.endDate();
    }

    private void validate(PromotionFileDto promotionFileDto) {
        validateEmpty(promotionFileDto.name());
        validateNumber(promotionFileDto.buy());
        validateNumber(promotionFileDto.get());
        validateDate(promotionFileDto.startDate());
        validateDate(promotionFileDto.endDate());
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

    private void validateDate(String str) {
        if (isNotDateFormat(str)) {
            throw CustomException.of(ErrorMessage.INVALID_DATE);
        }
    }

    private boolean isEmptyOrBlank(String str) {
        return str == null || str.isBlank();
    }

    private boolean isNotPositiveInteger(String str) {
        return !str.matches("\\d+") || Integer.parseInt(str) <= 0;
    }

    private boolean isNotDateFormat(String str) {
        try {
            LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return false;
        } catch (DateTimeParseException e) {
            return true;
        }
    }
}
