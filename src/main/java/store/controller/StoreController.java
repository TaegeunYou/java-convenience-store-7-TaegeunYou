package store.controller;

import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.List;
import store.domain.BuyProductResult;
import store.domain.BuyProductsResult;
import store.domain.Product;
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
            List<BuyProductDto> buyProducts = inputView.requestBuyProducts(products);
            buy(products, buyProducts);
        }
    }

    private void buy(Products products, List<BuyProductDto> buyProductDtos) {
        BuyProductsResult buyProductsResult = products.buyProducts(buyProductDtos);
        buyProductsResult = applyPromotionQuantitySufficient(buyProductsResult);
        buyProductsResult = determinePartialPromotion(buyProductsResult);
        applyPurchaseReduction(buyProductsResult);
    }

    private BuyProductsResult applyPromotionQuantitySufficient(BuyProductsResult buyProductsResult) {
        List<BuyProductResult> newBuyProductResults = new ArrayList<>();
        for (BuyProductResult buyProductResult : buyProductsResult.getBuyProducts()) {
            if (buyProductResult.isPromotionQuantityInsufficient()) {
                System.out.println("해택 받아서 더 가져올거야?");
                if (Console.readLine().equals("Y")) {
                    int getMoreProduct = buyProductResult.getPromotion().getGet();
                    BuyProductResult newBuyProductResult = new BuyProductResult(
                            buyProductResult,
                            buyProductResult.getProductForPromotionStock() + getMoreProduct,
                            buyProductResult.getTotalQuantity() + getMoreProduct
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
                System.out.println("해택 안받은거 있는데 그래도 구매할거야?");
                if (Console.readLine().equals("N")) {
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

    private void applyPurchaseReduction(BuyProductsResult buyProductsResult) {
        for (BuyProductResult buyProductResult : buyProductsResult.getBuyProducts()) {
            Product product = buyProductResult.getProduct();
            if (product.hasActivePromotion()) {
                product.minusPromotionStock(buyProductResult.getProductForPromotionStock());
                product.minusNormalStock(buyProductResult.getProductForNormalStock());
                continue;
            }
            int getProductForNormalStock = Math.min(buyProductResult.getTotalQuantity(), product.getNormalQuantity());
            int getProductForPromotionStock = buyProductResult.getTotalQuantity() - getProductForNormalStock;
            product.minusNormalStock(getProductForNormalStock);
            product.minusPromotionStock(getProductForPromotionStock);
        }
    }
}
