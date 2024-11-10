package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.dto.BuyProductDto;
import store.global.constants.ErrorMessage;
import store.global.constants.InputMessage;
import store.global.exception.CustomException;

public class InputView {
    private static final String BUY_PRODUCT_FORMAT_REGEX = "\\[(\\p{L}+)-(\\d+)](,\\[(\\p{L}+)-(\\d+)])*";
    private static final String BUY_PRODUCT_PARSE_FORMAT_REGEX = "\\[(\\p{L}+)-(\\d+)]";

    public List<BuyProductDto> requestBuyProducts() {
        while (true) {
            System.out.println(InputMessage.INPUT_BUY_PRODUCT.getMessage());
            try {
                List<BuyProductDto> buyProducts = enterBuyProduct();
                System.out.println();
                return buyProducts;
            } catch (CustomException e) {
                System.out.println(e.getMessage() + "\n");
            }
        }
    }

    private List<BuyProductDto> enterBuyProduct() {
        String input = Console.readLine();
        validateStringFormat(input);
        validateBuyProductFormat(input);
        return parseBuyProducts(input);
    }

    private void validateStringFormat(String str) {
        if (isEmptyOrBlank(str)) {
            throw CustomException.of(ErrorMessage.BLANK_INPUT);
        }
    }

    private void validateBuyProductFormat(String str) {
        if (!str.matches(BUY_PRODUCT_FORMAT_REGEX)) {
            throw CustomException.of(ErrorMessage.INVALID_BUY_PRODUCT);
        }
    }

    public List<BuyProductDto> parseBuyProducts(String input) {
        List<BuyProductDto> buyProducts = new ArrayList<>();
        Matcher matcher = Pattern.compile(BUY_PRODUCT_PARSE_FORMAT_REGEX).matcher(input);
        while (matcher.find()) {
            String name = matcher.group(1);
            int quantity = Integer.parseInt(matcher.group(2));
            buyProducts.add(new BuyProductDto(name, quantity));
        }
        return buyProducts;
    }

    private boolean isEmptyOrBlank(String str) {
        return str == null || str.isBlank();
    }
}
