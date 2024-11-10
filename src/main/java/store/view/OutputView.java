package store.view;

import store.domain.Product;
import store.domain.Products;

public class OutputView {
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
        String productFormat = "- %s %s원 %s개%s";

        for (Product product : products.getProducts()) {
            String str = String.format(
                    productFormat,
                    product.getName(),
                    String.format("%,d", product.getPrice()),
                    getProductQuantityPrintFormat(product.getQuantity()),
                    getProductPromotionPrintFormat(product.getPromotion())
            );
            System.out.println(str);
        }
        System.out.println();
    }

    private String getProductQuantityPrintFormat(int quantity) {
        if (quantity == 0) {
            return "재고 없음";
        }
        return String.valueOf(quantity);
    }

    private String getProductPromotionPrintFormat(String promotion) {
        if (promotion == null) {
            return "";
        }
        return " " + promotion;
    }
}
