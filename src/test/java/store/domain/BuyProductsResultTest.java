package store.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.global.file.dto.ProductFileDto;
import store.global.file.dto.PromotionFileDto;

public class BuyProductsResultTest {
    private BuyProductResult productWithPromotion;
    private BuyProductResult productWithoutPromotion;
    private BuyProductsResult buyProductsResult;

    @BeforeEach
    void setUp() {
        // given
        Product product1 = new Product(
                new ProductFileDto("콜라", "1000", "10", "탄산2+1"),
                new Promotion(new PromotionFileDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31"))
        );
        productWithPromotion = new BuyProductResult(product1.getPromotion(), 2, 5, product1);

        Product product2 = new Product(
                new ProductFileDto("사이다", "800", "10", null)
        );
        productWithoutPromotion = new BuyProductResult(null, 0, 3, product2);

        buyProductsResult = new BuyProductsResult(List.of(productWithPromotion, productWithoutPromotion));
    }

    @Test
    @DisplayName("구매한 상품 목록을 올바르게 반환한다.")
    void 구매한_상품_목록_반환() {
        // when
        List<BuyProductResult> result = buyProductsResult.getBuyProducts();

        // then
        assertThat(result).containsExactly(productWithPromotion, productWithoutPromotion);
    }

    @Test
    @DisplayName("멤버십 적용 시 할인 금액이 올바르게 계산된다.")
    void 멤버십_할인_적용() {
        // given
        buyProductsResult.applyMembership(true);

        // when
        int discountPrice = buyProductsResult.getMembershipDiscountPrice();

        // then
        int expectedTotalPriceWithoutBenefits = productWithPromotion.getTotalPrice() + productWithoutPromotion.getTotalPrice();
        int expectedDiscount = Math.min(expectedTotalPriceWithoutBenefits * 30 / 100, 8000);
        assertThat(discountPrice).isEqualTo(expectedDiscount);
    }

    @Test
    @DisplayName("멤버십 미적용 시 할인 금액은 0이 된다.")
    void 멤버십_미적용_할인_없음() {
        // given
        buyProductsResult.applyMembership(false);

        // when
        int discountPrice = buyProductsResult.getMembershipDiscountPrice();

        // then
        assertThat(discountPrice).isEqualTo(0);
    }

    @Test
    @DisplayName("총 가격이 올바르게 계산된다.")
    void 총_가격_계산() {
        // when
        int totalPrice = buyProductsResult.getTotalPrice();

        // then
        int expectedTotalPrice = productWithPromotion.getTotalPrice() + productWithoutPromotion.getTotalPrice();
        assertThat(totalPrice).isEqualTo(expectedTotalPrice);
    }

    @Test
    @DisplayName("총 수량이 올바르게 계산된다.")
    void 총_수량_계산() {
        // when
        int totalQuantity = buyProductsResult.getTotalQuantity();

        // then
        int expectedTotalQuantity = productWithPromotion.getTotalQuantity() + productWithoutPromotion.getTotalQuantity();
        assertThat(totalQuantity).isEqualTo(expectedTotalQuantity);
    }

    @Test
    @DisplayName("프로모션 혜택에 따른 할인 금액이 올바르게 계산된다.")
    void 프로모션_혜택_할인_계산() {
        // when
        int permissionDiscountPrice = buyProductsResult.getPermissionDiscountPrice();

        // then
        int expectedDiscountPrice = productWithPromotion.getForFree() * productWithPromotion.getProduct().getPrice();
        assertThat(permissionDiscountPrice).isEqualTo(expectedDiscountPrice);
    }

    @Test
    @DisplayName("최종 결제 금액이 올바르게 계산된다.")
    void 최종_결제_금액_계산() {
        // given
        buyProductsResult.applyMembership(true);

        // when
        int totalPay = buyProductsResult.getTotalPay();

        // then
        int expectedTotalPay = buyProductsResult.getTotalPrice()
                - buyProductsResult.getPermissionDiscountPrice()
                - buyProductsResult.getMembershipDiscountPrice();
        assertThat(totalPay).isEqualTo(expectedTotalPay);
    }
}