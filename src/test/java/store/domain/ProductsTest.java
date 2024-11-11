package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.dto.BuyProductDto;
import store.global.constants.ErrorMessage;
import store.global.exception.CustomException;
import store.global.file.dto.ProductFileDto;
import store.global.file.dto.PromotionFileDto;

public class ProductsTest {

    private Products products;
    private Product productWithPromotion;
    private Product productWithoutPromotion;
    private Promotions promotions;

    @BeforeEach
    void setUp() {
        // given
        PromotionFileDto promotionDto = new PromotionFileDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31");
        Promotion promotion = new Promotion(promotionDto);
        promotions = new Promotions(List.of(promotionDto));

        ProductFileDto productWithPromoDto = new ProductFileDto("콜라", "1000", "10", "탄산2+1");
        ProductFileDto productWithoutPromoDto = new ProductFileDto("사이다", "800", "10", null);

        productWithPromotion = new Product(productWithPromoDto, promotion);
        productWithoutPromotion = new Product(productWithoutPromoDto);

        products = new Products(List.of(productWithPromoDto, productWithoutPromoDto), promotions);
    }

    @Test
    @DisplayName("구매하려는 상품이 재고에 존재하지 않으면 예외를 발생시킨다.")
    void 구매_상품이_존재하지_않으면_예외() {
        // given
        List<BuyProductDto> buyProductDtos = List.of(new BuyProductDto("없는 상품", 1));

        // when & then
        assertThatThrownBy(() -> products.validateBuyProduct(buyProductDtos))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorMessage.PRODUCT_NOT_EXIST.getMessage());
    }

    @Test
    @DisplayName("구매하려는 상품 수량이 재고를 초과하면 예외를 발생시킨다.")
    void 구매_상품_수량_재고_초과시_예외() {
        // given
        List<BuyProductDto> buyProductDtos = List.of(new BuyProductDto("콜라", 20));

        // when & then
        assertThatThrownBy(() -> products.validateBuyProduct(buyProductDtos))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorMessage.STOCK_LIMIT_EXCEEDED.getMessage());
    }

    @Test
    @DisplayName("구매하려는 상품이 재고 내에 존재하며, 수량도 충분할 경우 예외가 발생하지 않는다.")
    void 구매_상품_재고_확인_성공() {
        // given
        List<BuyProductDto> buyProductDtos = List.of(new BuyProductDto("콜라", 5), new BuyProductDto("사이다", 3));

        // when & then
        products.validateBuyProduct(buyProductDtos);
    }

    @Test
    @DisplayName("구매 후 재고가 정확히 감소한다.")
    void 구매_후_재고_감소() {
        // given
        BuyProductDto buyProductDto = new BuyProductDto("콜라", 2);
        BuyProductsResult buyProductsResult = products.buyProducts(List.of(buyProductDto));

        // when
        products.applyPurchaseReduction(buyProductsResult);

        // then
        Product updatedProduct = products.getProducts().stream()
                .filter(p -> p.getName().equals("콜라"))
                .findFirst()
                .orElseThrow();
        assertThat(updatedProduct.getPromotionQuantity()).isEqualTo(productWithPromotion.getPromotionQuantity() - 2);
    }

    @Test
    @DisplayName("상품 구매 시 프로모션이 있는 상품과 없는 상품에 대한 BuyProductResult를 생성한다.")
    void 상품_구매_결과_생성() {
        // given
        List<BuyProductDto> buyProductDtos = List.of(
                new BuyProductDto("콜라", 2),
                new BuyProductDto("사이다", 3)
        );

        // when
        BuyProductsResult buyProductsResult = products.buyProducts(buyProductDtos);

        // then
        assertThat(buyProductsResult.getBuyProducts().size()).isEqualTo(2);
        assertThat(buyProductsResult.getBuyProducts().get(0).getProduct().getName()).isEqualTo("콜라");
        assertThat(buyProductsResult.getBuyProducts().get(1).getProduct().getName()).isEqualTo("사이다");
    }

    @Test
    @DisplayName("상품 목록을 올바르게 반환한다.")
    void 상품_목록_반환() {
        // when
        List<Product> productList = products.getProducts();

        // then
        assertThat(productList.get(0).getName()).isEqualTo(productWithPromotion.getName());
        assertThat(productList.get(1).getName()).isEqualTo(productWithoutPromotion.getName());
    }
}
