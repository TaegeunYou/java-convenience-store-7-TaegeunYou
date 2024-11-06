package store.global.constants;

public enum ErrorMessage {
    FILE_NOT_FOUND("파일을 찾을 수 없습니다."),
    FILE_EMPTY("파일이 비어있습니다."),
    BLANK_INPUT("빈 문자열이 입력되었습니다."),
    INVALID_INPUT_DETAIL_COUNT("입력된 상세 정보 개수가 올바르지 않습니다."),
    EMPTY_NOT_ALLOWED("해당 값이 없거나 공백일 수 없습니다"),
    BLANK_NOT_ALLOWED("해당 값이 공백일 수 없습니다"),
    INVALID_NUMBER("해당 값은 양의 정수여야 합니다."),
    INVALID_DATE("해당 값은 날짜 형식이여야 합니다.");

    private final static String PREFIX = "[ERROR]";
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return PREFIX + " " + this.message;
    }
    }
