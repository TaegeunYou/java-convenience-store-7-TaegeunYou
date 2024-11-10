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

    //프로모션_재고중에_공짜인거
    public int getForFree() {
        if (!product.hasActivePromotion()) {
            return 0;
        }
        return getProductForPromotionStock / (promotion.getBuy() + promotion.getGet());
    }

    //차감하고_남은_프로모션_재고
    public int getRemainPromotionStock() {
        return product.getPromotionQuantity() - getProductForPromotionStock;
    }

    //프로모션_재고에서_해택_받고_가져온거
    public int getPromotionBenefitQuantity() {
        if (!product.hasActivePromotion()) {
            return 0;
        }
        return getForFree() * (promotion.getBuy() + promotion.getGet());
    }

    //프로모션_해택받지_않는_개수
    public int getNonBenefitQuantity() {
        return totalQuantity - getPromotionBenefitQuantity();
    }

    //일반_재고에서_가져올거
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
                    this,
                    getProductForPromotionStock() + getMoreQuantity,
                    getTotalQuantity() + getMoreQuantity
            );
        }
        return this;
    }

    public BuyProductResult applyPartialPromotion(boolean isFullBuy) {
        if (!isFullBuy) {
            return new BuyProductResult(
                    this,
                    getPromotionBenefitQuantity(),
                    getPromotionBenefitQuantity()
            );
        }
        return this;
    }
}
