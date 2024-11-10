package store.view;

import store.domain.Product;
import store.domain.Products;

public class OutputView {
    private static final String PRODUCT_NORMAL_FORMAT = "- %s %s원 %s";
    private static final String PRODUCT_PROMOTION_FORMAT = "- %s %s원 %s %s";

    public void printlnMessage(String message) {
        System.out.println(message);
    }

    public void printEmptyLine() {
        System.out.println();
    }

    public void printlnMessageWithEmptyLine(String message) {
        printlnMessage(message + "\n");
    }

    public void printProducts(Products products) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        for (Product product : products.getProducts()) {
            printProduct(product);
        }
        System.out.println();
    }

    private void printProduct(Product product) {
        if (product.getPromotion() != null) {
            printPromotionProduct(product);
        }
        printNormalProduct(product);
    }

    private void printPromotionProduct(Product product) {
        String str = String.format(PRODUCT_PROMOTION_FORMAT, product.getName(),
                getProductPriceFormat(product.getPrice()),
                getProductQuantityPrintFormat(product.getPromotionQuantity()),
                product.getPromotion().getName()
        );
        System.out.println(str);
    }

    private void printNormalProduct(Product product) {
        String str = String.format(PRODUCT_NORMAL_FORMAT, product.getName(),
                getProductPriceFormat(product.getPrice()),
                getProductQuantityPrintFormat(product.getNormalQuantity())
        );
        System.out.println(str);
    }

    private String getProductPriceFormat(int price) {
        return String.format("%,d", price);
    }

    private String getProductQuantityPrintFormat(int quantity) {
        if (quantity == 0) {
            return "재고 없음";
        }
        return quantity + "개";
    }
}
