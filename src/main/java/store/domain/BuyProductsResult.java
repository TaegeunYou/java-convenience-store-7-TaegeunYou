package store.domain;

import java.util.List;

public class BuyProductsResult {
    private final List<BuyProductResult> buyProductResults;
    private boolean hasMembership;
    private final int MEMBERSHIP_DISCOUNT_PERCENTAGE = 30;
    private final int MAX_MEMBERSHIP_DISCOUNT_PRICE = 8000;

    public BuyProductsResult(List<BuyProductResult> buyProductResults) {
        this.buyProductResults = buyProductResults;
    }

    public List<BuyProductResult> getBuyProducts() {
        return buyProductResults;
    }

    public void applyMembership(boolean isMemberShipDiscountApply) {
        this.hasMembership = isMemberShipDiscountApply;
    }

    public int getMembershipDiscountPrice() {
        int totalPrice = 0;
        for (BuyProductResult buyProductResult : buyProductResults) {
            int quantity = buyProductResult.getTotalQuantity() - buyProductResult.getPromotionBenefitQuantity();
            int price = buyProductResult.getProduct().getPrice() * quantity;
            totalPrice += price;
        }
        int discountPrice = totalPrice * MEMBERSHIP_DISCOUNT_PERCENTAGE / 100;
        return Math.min(discountPrice, MAX_MEMBERSHIP_DISCOUNT_PRICE);
    }
}
