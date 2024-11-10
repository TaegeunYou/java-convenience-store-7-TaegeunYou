package store.domain;

import java.util.List;

public class BuyProductsResult {
    private final List<BuyProductResult> buyProductResults;

    public BuyProductsResult(List<BuyProductResult> buyProductResults) {
        this.buyProductResults = buyProductResults;
    }

    public List<BuyProductResult> getBuyProducts() {
        return buyProductResults;
    }
}
