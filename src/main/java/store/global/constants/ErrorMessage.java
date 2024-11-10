package store.global.constants;

public enum ErrorMessage {
    FILE_NOT_FOUND("파일을 찾을 수 없습니다."),
    FILE_EMPTY("파일이 비어있습니다."),
    BLANK_INPUT("빈 문자열이 입력되었습니다."),
    INVALID_INPUT_DETAIL_COUNT("입력된 상세 정보 개수가 올바르지 않습니다."),
    EMPTY_NOT_ALLOWED("해당 값이 없거나 공백일 수 없습니다"),
    BLANK_NOT_ALLOWED("해당 값이 공백일 수 없습니다"),
    INVALID_NUMBER("해당 값은 양의 정수여야 합니다."),
    INVALID_DATE("해당 값은 날짜 형식이여야 합니다."),
    INVALID_BUY_PRODUCT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    BUY_PRODUCT_DUPLICATE("동일한 상품은 하나로 묶어서 다시 입력해 주세요."),
    PRODUCT_NOT_FOUND("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    STOCK_LIMIT_EXCEEDED("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    NOT_FOUND_PRODUCT("상품 정보를 찾을 수 없습니다");

    private final static String PREFIX = "[ERROR]";
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return PREFIX + " " + this.message;
    }
    }
