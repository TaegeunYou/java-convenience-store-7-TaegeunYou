package store.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.global.file.dto.ProductFileDto;
import store.global.file.dto.PromotionFileDto;

public class BuyProductResultTest {
    private static final Product TEST_PRODUCT_WITH_PROMOTION = new Product(
            new ProductFileDto("콜라", "1000", "10", "탄산2+1"),
            new Promotion(new PromotionFileDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31"))
    );
    private static final Product TEST_PRODUCT_WITHOUT_PROMOTION = new Product(
            new ProductFileDto("사이다", "800", "10", null)
    );

    @Test
    @DisplayName("프로모션이 있는 상품의 BuyProductResult가 정상적으로 생성된다.")
    void 프로모션이_있는_상품의_BuyProductResult_생성() {
        // given & when
        BuyProductResult buyProductResult = new BuyProductResult(
                TEST_PRODUCT_WITH_PROMOTION.getPromotion(), 2, 5, TEST_PRODUCT_WITH_PROMOTION
        );

        // then
        assertThat(buyProductResult.getProduct()).isEqualTo(TEST_PRODUCT_WITH_PROMOTION);
        assertThat(buyProductResult.getPromotion()).isEqualTo(TEST_PRODUCT_WITH_PROMOTION.getPromotion());
        assertThat(buyProductResult.getTotalQuantity()).isEqualTo(5);
        assertThat(buyProductResult.getProductForPromotionStock()).isEqualTo(2);
    }

    @Test
    @DisplayName("프로모션 수량이 부족할 때 isPromotionQuantityInsufficient가 true를 반환한다.")
    void 프로모션_수량_부족_확인() {
        // given
        BuyProductResult buyProductResult = new BuyProductResult(
                TEST_PRODUCT_WITH_PROMOTION.getPromotion(), 2, 5, TEST_PRODUCT_WITH_PROMOTION
        );

        // when
        boolean result = buyProductResult.isPromotionQuantityInsufficient();

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("프로모션이 없는 경우 isPromotionQuantityInsufficient가 false를 반환한다.")
    void 프로모션_없는_경우_수량_부족_확인() {
        // given
        BuyProductResult buyProductResult = new BuyProductResult(
                null, 0, 5, TEST_PRODUCT_WITHOUT_PROMOTION
        );

        // when
        boolean result = buyProductResult.isPromotionQuantityInsufficient();

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("부분 프로모션이 적용 가능한 경우 isPartialPromotionApplicable가 true를 반환한다.")
    void 부분_프로모션_적용_가능성_확인() {
        // given
        BuyProductResult buyProductResult = new BuyProductResult(
                TEST_PRODUCT_WITH_PROMOTION.getPromotion(), 1, 3, TEST_PRODUCT_WITH_PROMOTION
        );

        // when
        boolean result = buyProductResult.isPartialPromotionApplicable();

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("프로모션이 적용되지 않은 경우 부분 프로모션이 적용되지 않는다.")
    void 프로모션_없는_경우_부분_프로모션_적용_불가() {
        // given
        BuyProductResult buyProductResult = new BuyProductResult(
                null, 0, 3, TEST_PRODUCT_WITHOUT_PROMOTION
        );

        // when
        boolean result = buyProductResult.isPartialPromotionApplicable();

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("총 가격이 올바르게 계산된다.")
    void 총_가격_계산() {
        // given
        BuyProductResult buyProductResult = new BuyProductResult(
                TEST_PRODUCT_WITH_PROMOTION.getPromotion(), 2, 5, TEST_PRODUCT_WITH_PROMOTION
        );

        // when
        int totalPrice = buyProductResult.getTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(buyProductResult.getTotalQuantity() * TEST_PRODUCT_WITH_PROMOTION.getPrice());
    }

    @Test
    @DisplayName("프로모션이 충분한 경우 applyPromotionIfSufficient가 프로모션을 추가하여 새로운 객체를 반환한다.")
    void 프로모션_충분시_applyPromotionIfSufficient() {
        // given
        BuyProductResult buyProductResult = new BuyProductResult(
                TEST_PRODUCT_WITH_PROMOTION.getPromotion(), 2, 5, TEST_PRODUCT_WITH_PROMOTION
        );

        // when
        BuyProductResult updatedResult = buyProductResult.applyPromotionIfSufficient(true);

        // then
        assertThat(updatedResult.getProductForPromotionStock()).isEqualTo(2 + TEST_PRODUCT_WITH_PROMOTION.getPromotion().getGet());
        assertThat(updatedResult.getTotalQuantity()).isEqualTo(5 + TEST_PRODUCT_WITH_PROMOTION.getPromotion().getGet());
    }

    @Test
    @DisplayName("프로모션이 충분하지 않은 경우 applyPromotionIfSufficient가 동일 객체를 반환한다.")
    void 프로모션_부족시_applyPromotionIfSufficient() {
        // given
        BuyProductResult buyProductResult = new BuyProductResult(
                TEST_PRODUCT_WITH_PROMOTION.getPromotion(), 2, 5, TEST_PRODUCT_WITH_PROMOTION
        );

        // when
        BuyProductResult updatedResult = buyProductResult.applyPromotionIfSufficient(false);

        // then
        assertThat(updatedResult).isSameAs(buyProductResult);
    }

    @Test
    @DisplayName("isFullBuy가 false일 때 applyPartialPromotion이 부분 프로모션 수량을 적용하여 새로운 객체를 반환한다.")
    void 부분_프로모션_적용() {
        // given
        BuyProductResult buyProductResult = new BuyProductResult(
                TEST_PRODUCT_WITH_PROMOTION.getPromotion(), 2, 5, TEST_PRODUCT_WITH_PROMOTION
        );

        // when
        BuyProductResult partialResult = buyProductResult.applyPartialPromotion(false);

        // then
        assertThat(partialResult.getProductForPromotionStock()).isEqualTo(buyProductResult.getPromotionBenefitQuantity());
        assertThat(partialResult.getTotalQuantity()).isEqualTo(buyProductResult.getPromotionBenefitQuantity());
    }

    @Test
    @DisplayName("isFullBuy가 true일 때 applyPartialPromotion이 동일 객체를 반환한다.")
    void 전체_프로모션_적용() {
        // given
        BuyProductResult buyProductResult = new BuyProductResult(
                TEST_PRODUCT_WITH_PROMOTION.getPromotion(), 2, 5, TEST_PRODUCT_WITH_PROMOTION
        );

        // when
        BuyProductResult fullResult = buyProductResult.applyPartialPromotion(true);

        // then
        assertThat(fullResult).isSameAs(buyProductResult);
    }
}