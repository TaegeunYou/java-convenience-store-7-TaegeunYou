package store.domain;

public class BuyProductResult {
    private final Product product;
    private final int totalQuantity;
    private final Promotion promotion;
    private final int getProductForPromotionStock;

    public BuyProductResult(Promotion promotion, int getProductForPromotionStock, int totalQuantity, Product product) {
        this.promotion = promotion;
        this.getProductForPromotionStock = getProductForPromotionStock;
        this.totalQuantity = totalQuantity;
        this.product = product;
    }

    public BuyProductResult(BuyProductResult buyProductResult, int getProductForPromotionStock, int totalQuantity) {
        this.promotion = buyProductResult.promotion;
        this.getProductForPromotionStock = getProductForPromotionStock;
        this.totalQuantity = totalQuantity;
        this.product = buyProductResult.product;
    }

    public boolean isPromotionQuantityInsufficient() {
        return product.hasActivePromotion()
                && getProductForPromotionStock - getPromotionBenefitQuantity() >= promotion.getBuy()
                && getRemainPromotionStock() >= promotion.getGet();
    }

    public boolean isPartialPromotionApplicable() {
        return product.hasActivePromotion() && getPromotionBenefitQuantity() != totalQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public int getProductForPromotionStock() {
        return getProductForPromotionStock;
    }

    public int getForFree() {
        if (!product.hasActivePromotion()) {
            return 0;
        }
        return getProductForPromotionStock / (promotion.getBuy() + promotion.getGet());
    }

    public int getRemainPromotionStock() {
        return product.getPromotionQuantity() - getProductForPromotionStock;
    }

    public int getPromotionBenefitQuantity() {
        if (!product.hasActivePromotion()) {
            return 0;
        }
        return getForFree() * (promotion.getBuy() + promotion.getGet());
    }

    public int getNonBenefitQuantity() {
        return totalQuantity - getPromotionBenefitQuantity();
    }

    public int getProductForNormalStock() {
        return totalQuantity - getProductForPromotionStock;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getTotalPrice() {
        return totalQuantity * product.getPrice();
    }

    public BuyProductResult applyPromotionIfSufficient(boolean isPromotionSufficient) {
        if (isPromotionSufficient) {
            int getMoreQuantity = getPromotion().getGet();
            return new BuyProductResult(
                    this, getProductForPromotionStock + getMoreQuantity,
                    totalQuantity + getMoreQuantity
            );
        }
        return this;
    }

    public BuyProductResult applyPartialPromotion(boolean isFullBuy) {
        if (!isFullBuy) {
            return new BuyProductResult(
                    this, getPromotionBenefitQuantity(), getPromotionBenefitQuantity()
            );
        }
        return this;
    }
}
