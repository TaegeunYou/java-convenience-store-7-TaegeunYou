package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import store.global.constants.ErrorMessage;
import store.global.exception.CustomException;
import store.global.file.dto.PromotionFileDto;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(PromotionFileDto promotionFileDto) {
        validate(promotionFileDto);
        this.name = promotionFileDto.name();
        this.buy = Integer.parseInt(promotionFileDto.buy());
        this.get = Integer.parseInt(promotionFileDto.get());
        this.startDate = LocalDate.parse(promotionFileDto.startDate());
        this.endDate = LocalDate.parse(promotionFileDto.endDate());
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
        return !str.matches("\\d+") || Integer.parseInt(str) < 0;
    }

    private boolean isNotDateFormat(String str) {
        try {
            LocalDate.parse(str);
            return false;
        } catch (DateTimeParseException e) {
            return true;
        }
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        LocalDateTime now = DateTimes.now();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MIN);
        return !now.isBefore(startDateTime) && !now.isAfter(endDateTime);
    }
}
