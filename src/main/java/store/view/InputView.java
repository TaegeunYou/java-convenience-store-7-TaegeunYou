package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.BuyProductResult;
import store.domain.Products;
import store.dto.BuyProductDto;
import store.global.constants.ErrorMessage;
import store.global.constants.InputMessage;
import store.global.exception.CustomException;

public class InputView {
    private static final String BUY_PRODUCT_FORMAT_REGEX = "^(\\[[\\p{L}a-zA-Z0-9]+-\\d+])(,(\\[[\\p{L}a-zA-Z0-9]+-\\d+]))*$";
    private static final String BUY_PRODUCT_PARSE_FORMAT_REGEX = "\\[([\\p{L}a-zA-Z0-9]+)-(\\d+)]";
    private static final String INPUT_TRUE_FORMAT = "Y";
    private static final String INPUT_FALSE_FORMAT = "N";

    public List<BuyProductDto> requestBuyProducts(Products products) {
        while (true) {
            try {
                return handleBuyProductRequest(products);
            } catch (CustomException e) {
                printErrorMessage(e);
            }
        }
    }

    public boolean requestPromotionQuantitySufficient(BuyProductResult buyProductResult) {
        while (true) {
            try {
                return handlePromotionQuantitySufficientRequest(buyProductResult);
            } catch (CustomException e) {
                printErrorMessage(e);
            }
        }
    }

    public boolean requestDetermineFullBuy(BuyProductResult buyProductResult) {
        while (true) {
            try {
                return handleDetermineFullBuyRequest(buyProductResult);
            } catch (CustomException e) {
                printErrorMessage(e);
            }
        }
    }

    public boolean requestMembership() {
        while (true) {
            try {
                return handleMembershipRequest();
            } catch (CustomException e) {
                printErrorMessage(e);
            }
        }
    }

    public boolean requestAdditionalBuy() {
        while (true) {
            try {
                return handleAdditionalBuyRequest();
            } catch (CustomException e) {
                printErrorMessage(e);
            }
        }
    }

    private List<BuyProductDto> handleBuyProductRequest(Products products) {
        System.out.println(InputMessage.INPUT_BUY_PRODUCT.getMessage());
        List<BuyProductDto> buyProducts = enterBuyProduct();
        products.validateBuyProduct(buyProducts);
        System.out.println();
        return buyProducts;
    }

    private boolean handlePromotionQuantitySufficientRequest(BuyProductResult buyProductResult) {
        String message = String.format(
                InputMessage.INPUT_PROMOTION_QUANTITY_SUFFICIENT.getMessage(),
                buyProductResult.getProduct().getName(), buyProductResult.getPromotion().getGet()
        );
        System.out.println(message);
        boolean isSufficient = enterPromotionQuantitySufficient();
        System.out.println();
        return isSufficient;
    }

    private boolean handleDetermineFullBuyRequest(BuyProductResult buyProductResult) {
        String message = String.format(
                InputMessage.INPUT_DETERMINE_FULL_BUY.getMessage(),
                buyProductResult.getProduct().getName(), buyProductResult.getNonBenefitQuantity()
        );
        System.out.println(message);
        boolean isFullBuy = enterDetermineFullBuy();
        System.out.println();
        return isFullBuy;
    }

    private boolean handleMembershipRequest() {
        System.out.println(InputMessage.INPUT_MEMBER_SHIP.getMessage());
        boolean memberShip = enterMemberShip();
        System.out.println();
        return memberShip;
    }

    private boolean handleAdditionalBuyRequest() {
        System.out.println(InputMessage.INPUT_ADDITIONAL_BUY.getMessage());
        boolean isAdditionalBuy = enterAdditionalBuy();
        System.out.println();
        return isAdditionalBuy;
    }

    private List<BuyProductDto> enterBuyProduct() {
        String input = Console.readLine();
        validateStringFormat(input);
        validateBuyProductFormat(input);
        return parseBuyProducts(input);
    }

    private boolean enterPromotionQuantitySufficient() {
        String input = Console.readLine();
        validateStringFormat(input);
        validateYesNoFormat(input);
        return parseYesNo(input);
    }

    private boolean enterDetermineFullBuy() {
        String input = Console.readLine();
        validateStringFormat(input);
        validateYesNoFormat(input);
        return parseYesNo(input);
    }

    private boolean enterMemberShip() {
        String input = Console.readLine();
        validateStringFormat(input);
        validateYesNoFormat(input);
        return parseYesNo(input);
    }

    private boolean enterAdditionalBuy() {
        String input = Console.readLine();
        validateStringFormat(input);
        validateYesNoFormat(input);
        return parseYesNo(input);
    }

    private boolean parseYesNo(String input) {
        return input.equals(INPUT_TRUE_FORMAT);
    }

    private void validateYesNoFormat(String input) {
        if (!input.equals(INPUT_TRUE_FORMAT) && !input.equals(INPUT_FALSE_FORMAT)) {
            throw CustomException.of(ErrorMessage.INVALID_YES_NO_FORMAT);
        }
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
            BuyProductDto buyProduct = createBuyProduct(matcher);
            validateAndAddProduct(buyProduct, buyProducts);
        }
        return buyProducts;
    }

    private BuyProductDto createBuyProduct(Matcher matcher) {
        String name = matcher.group(1);
        int quantity = Integer.parseInt(matcher.group(2));
        return new BuyProductDto(name, quantity);
    }

    private void validateAndAddProduct(BuyProductDto buyProduct, List<BuyProductDto> buyProducts) {
        validateDuplicateBuyProduct(buyProduct, buyProducts);
        buyProducts.add(buyProduct);
    }

    private boolean isEmptyOrBlank(String str) {
        return str == null || str.isBlank();
    }

    private void validateDuplicateBuyProduct(BuyProductDto buyProduct, List<BuyProductDto> buyProducts) {
        if (isBuyProductDuplicate(buyProduct, buyProducts)) {
            throw CustomException.of(ErrorMessage.BUY_PRODUCT_DUPLICATE);
        }
    }

    private boolean isBuyProductDuplicate(BuyProductDto buyProduct, List<BuyProductDto> buyProducts) {
        return buyProducts.stream().anyMatch(product -> product.name().equals(buyProduct.name()));
    }

    private void printErrorMessage(CustomException exception) {
        System.out.println(exception.getMessage() + "\n");
    }
}
