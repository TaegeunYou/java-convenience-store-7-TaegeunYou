package store.global.constants;

public enum ErrorMessage {
    FILE_NOT_FOUND("파일을 찾을 수 없습니다."),
    FILE_EMPTY("파일이 비어있습니다."),
    BLANK_INPUT("빈 문자열이 입력되었습니다.");

    private final static String PREFIX = "[ERROR]";
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return PREFIX + " " + this.message;
    }
    }
