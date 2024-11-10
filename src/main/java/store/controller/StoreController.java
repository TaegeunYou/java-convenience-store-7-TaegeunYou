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
        processProductBuy(products);
    }

    private void processProductBuy(Products products) {
        do {
            outputView.printProducts(products);
            List<BuyProductDto> buyProductDtos = inputView.requestBuyProducts(products);
            BuyProductsResult buyProductsResult = buyProducts(products, buyProductDtos);
            buyProductsResult.applyMembership(inputView.requestMembership());
            outputView.printBuyProductsResult(buyProductsResult);
        } while (inputView.requestAdditionalBuy());
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
            BuyProductResult updatedResult = processProductPromotion(buyProductResult);
            newBuyProductResults.add(updatedResult);
        }
        return new BuyProductsResult(newBuyProductResults);
    }

    private BuyProductResult processProductPromotion(BuyProductResult buyProductResult) {
        if (buyProductResult.isPromotionQuantityInsufficient()) {
            boolean isPromotionSufficient = inputView.requestPromotionQuantitySufficient(buyProductResult);
            return buyProductResult.applyPromotionIfSufficient(isPromotionSufficient);
        }
        return buyProductResult;
    }

    private BuyProductsResult determinePartialPromotion(BuyProductsResult buyProductsResult) {
        List<BuyProductResult> newBuyProductResults = new ArrayList<>();
        for (BuyProductResult buyProductResult : buyProductsResult.getBuyProducts()) {
            BuyProductResult updatedResult = processPartialPromotion(buyProductResult);
            newBuyProductResults.add(updatedResult);
        }
        return new BuyProductsResult(newBuyProductResults);
    }

    private BuyProductResult processPartialPromotion(BuyProductResult buyProductResult) {
        if (buyProductResult.isPartialPromotionApplicable()) {
            boolean isFullBuy = inputView.requestDetermineFullBuy(buyProductResult);
            return buyProductResult.applyPartialPromotion(isFullBuy);
        }
        return buyProductResult;
    }

}
