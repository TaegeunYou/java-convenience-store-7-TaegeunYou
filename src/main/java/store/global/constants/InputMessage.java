package store.global.constants;

public enum InputMessage {

    INPUT_BUY_PRODUCT("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    INPUT_MEMBER_SHIP("멤버십 할인을 받으시겠습니까? (Y/N)");

    private String message;

    InputMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
