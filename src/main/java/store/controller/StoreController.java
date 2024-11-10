package store.controller;

import java.util.ArrayList;
import java.util.List;
import store.domain.BuyProductResult;
import store.domain.BuyProductsResult;
import store.domain.Products;
import store.domain.Promotions;
import store.dto.BuyProductDto;
import store.global.constants.FilePath;
import store.global.file.ReadProductFile;
import store.global.file.ReadPromotionFile;
import store.global.file.dto.ProductFileDto;
import store.global.file.dto.PromotionFileDto;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final ReadProductFile readProductFile;
    private final ReadPromotionFile readPromotionFile;
    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(
            ReadProductFile readProductFile, ReadPromotionFile readPromotionFile,
            InputView inputView, OutputView outputView
    ) {
        this.readProductFile = readProductFile;
        this.readPromotionFile = readPromotionFile;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void execute() {
        List<ProductFileDto> productDtos = readProductFile.getProductDtos(FilePath.PRODUCTS.getPath());
        List<PromotionFileDto> promotionsDtos = readPromotionFile.getPromotionsDtos(FilePath.PROMOTIONS.getPath());
        Promotions promotions = new Promotions(promotionsDtos);
        Products products = new Products(productDtos, promotions);

        while (true) {
            outputView.printProducts(products);
            List<BuyProductDto> buyProductDtos = inputView.requestBuyProducts(products);
            BuyProductsResult buyProductsResult = buyProducts(products, buyProductDtos);
            buyProductsResult.applyMembership(inputView.requestMembership());
            outputView.printBuyProductsResult(buyProductsResult);
            if (!inputView.requestAdditionalBuy()) {
                break;
            }
        }
    }

    private BuyProductsResult buyProducts(Products products, List<BuyProductDto> buyProductDtos) {
        BuyProductsResult buyProductsResult = products.buyProducts(buyProductDtos);
        buyProductsResult = applyPromotionQuantitySufficient(buyProductsResult);
        buyProductsResult = determinePartialPromotion(buyProductsResult);
        products.applyPurchaseReduction(buyProductsResult);
        return buyProductsResult;
    }

    private BuyProductsResult applyPromotionQuantitySufficient(BuyProductsResult buyProductsResult) {
        List<BuyProductResult> newBuyProductResults = new ArrayList<>();
        for (BuyProductResult buyProductResult : buyProductsResult.getBuyProducts()) {
            if (buyProductResult.isPromotionQuantityInsufficient()) {
                if (inputView.requestPromotionQuantitySufficient(buyProductResult)) {
                    int getMoreQuantity = buyProductResult.getPromotion().getGet();
                    BuyProductResult newBuyProductResult = new BuyProductResult(
                            buyProductResult,
                            buyProductResult.getProductForPromotionStock() + getMoreQuantity,
                            buyProductResult.getTotalQuantity() + getMoreQuantity
                    );
                    newBuyProductResults.add(newBuyProductResult);
                    continue;
                }
            }
            newBuyProductResults.add(buyProductResult);
        }
        return new BuyProductsResult(newBuyProductResults);
    }

    private BuyProductsResult determinePartialPromotion(BuyProductsResult buyProductsResult) {
        List<BuyProductResult> newBuyProductResults = new ArrayList<>();
        for (BuyProductResult buyProductResult : buyProductsResult.getBuyProducts()) {
            if (buyProductResult.isPartialPromotionApplicable()) {
                if (!inputView.requestDetermineFullBuy(buyProductResult)) {
                    BuyProductResult newBuyProductResult = new BuyProductResult(
                            buyProductResult,
                            buyProductResult.getPromotionBenefitQuantity(),
                            buyProductResult.getPromotionBenefitQuantity()
                    );
                    newBuyProductResults.add(newBuyProductResult);
                }
            }
            newBuyProductResults.add(buyProductResult);
        }
        return new BuyProductsResult(newBuyProductResults);
    }
}
