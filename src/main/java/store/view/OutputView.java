package store.view;

import store.domain.BuyProductResult;
import store.domain.BuyProductsResult;
import store.domain.Product;
import store.domain.Products;

public class OutputView {
    private static final String PRODUCT_NORMAL_FORMAT = "- %s %s원 %s";
    private static final String PRODUCT_PROMOTION_FORMAT = "- %s %s원 %s %s";

    public void printProducts(Products products) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        for (Product product : products.getProducts()) {
            printProduct(product);
        }
        System.out.println();
    }

    public void printBuyProductsResult(BuyProductsResult buyProductsResult) {
        System.out.println("==============W 편의점================");
        System.out.println("상품명 수량 금액");
        printBuyProduct(buyProductsResult);
        System.out.println("=============증정===============");
        printBuyProductFree(buyProductsResult);
        System.out.println("====================================");
        printResult(buyProductsResult);
        System.out.println();
    }

    private void printProduct(Product product) {
        if (product.hasPromotion()) {
            printPromotionProduct(product);
        }
        printNormalProduct(product);
    }

    private void printPromotionProduct(Product product) {
        String str = String.format(PRODUCT_PROMOTION_FORMAT, product.getName(),
                getPriceFormat(product.getPrice()),
                getProductQuantityPrintFormat(product.getPromotionQuantity()),
                product.getPromotion().getName()
        );
        System.out.println(str);
    }

    private void printNormalProduct(Product product) {
        String str = String.format(PRODUCT_NORMAL_FORMAT, product.getName(),
                getPriceFormat(product.getPrice()),
                getProductQuantityPrintFormat(product.getNormalQuantity())
        );
        System.out.println(str);
    }

    private String getPriceFormat(int price) {
        return String.format("%,d", price);
    }

    private String getProductQuantityPrintFormat(int quantity) {
        if (quantity == 0) {
            return "재고 없음";
        }
        return quantity + "개";
    }

    private void printBuyProduct(BuyProductsResult buyProductsResult) {
        for (BuyProductResult buyProduct : buyProductsResult.getBuyProducts()) {
            Product product = buyProduct.getProduct();
            System.out.println(product.getName() + " " + buyProduct.getTotalQuantity() + " " + getPriceFormat(
                    buyProduct.getTotalPrice()));
        }
    }

    private void printBuyProductFree(BuyProductsResult buyProductsResult) {
        for (BuyProductResult buyProduct : buyProductsResult.getBuyProducts()) {
            if (buyProduct.getForFree() != 0) {
                System.out.println(buyProduct.getProduct().getName() + " " + buyProduct.getForFree());
            }
        }
    }

    private void printResult(BuyProductsResult buyProductsResult) {
        System.out.println("총구매액 " + buyProductsResult.getTotalQuantity() + " " + getPriceFormat(
                buyProductsResult.getTotalPrice()));
        System.out.println("행사할인 -" + getPriceFormat(buyProductsResult.getPermissionDiscountPrice()));
        System.out.println("멤버십할인 -" + getPriceFormat(buyProductsResult.getMembershipDiscountPrice()));
        System.out.println("내실돈 " + getPriceFormat(buyProductsResult.getTotalPay()));
    }
}
