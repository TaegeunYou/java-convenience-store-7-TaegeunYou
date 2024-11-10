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
    public int getGetForFree() {
        return getProductForPromotionStock / (promotion.getBuy() + promotion.getGet());
    }

    //차감하고_남은_프로모션_재고
    public int getRemainPromotionStock() {
        return product.getPromotionQuantity() - getProductForPromotionStock;
    }

    //프로모션_재고에서_해택_받고_가져온거
    public int getPromotionBenefitQuantity() {
        return getGetForFree() * (promotion.getBuy() + promotion.getGet());
    }

    //일반_재고에서_가져올거
    public int getProductForNormalStock() {
        return totalQuantity - getProductForPromotionStock;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }
}
