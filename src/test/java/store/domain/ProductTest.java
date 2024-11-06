package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.global.constants.ErrorMessage;
import store.global.file.dto.ProductFileDto;

public class ProductTest {
    private static final String CORRECT_NAME = "콜라";
    private static final String CORRECT_PRICE = "1000";
    private static final String CORRECT_QUANTITY = "10";
    private static final String CORRECT_PROMOTION = "탄산2+1";

    @ParameterizedTest
    @CsvSource(value = {
            "콜라,1000,10,탄산2+1",
            "사이다,1000,8,탄산2+1",
            "오렌지주스,1800,9,MD추천상품"
    }, delimiter = ',')
    @DisplayName("상품을 정상적으로 생성하는지 확인한다.")
    void 상품을_정상적으로_생성하는지_확인한다(String name, String price, String quantity, String promotion) {
        // given
        ProductFileDto productFileDto = new ProductFileDto(name, price, quantity, promotion);

        // when
        Product product = new Product(productFileDto);

        // then
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(Integer.parseInt(price));
        assertThat(product.getQuantity()).isEqualTo(Integer.parseInt(quantity));
        assertThat(product.getPromotion()).isEqualTo(promotion);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("상품 이름에 빈 문자열이나 공백이 들어오면 에러를 반환한다.")
    void 상품_이름에_빈_문자열이나_공백이_들어오면_에러를_반환한다(String input) {
        // given
        ProductFileDto productFileDto = new ProductFileDto(input, CORRECT_PRICE, CORRECT_QUANTITY, CORRECT_PROMOTION);

        // when & then
        assertThatThrownBy(() -> new Product(productFileDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.EMPTY_NOT_ALLOWED.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"콜라", "-1000"})
    @DisplayName("상품 가격에 숫자가 아닌 값이나 음수가 들어오면 에러를 반환한다.")
    void 상품_가격에_숫자가_아닌_값이나_음수가_들어오면_에러를_반환한다(String input) {
        // given
        ProductFileDto productFileDto = new ProductFileDto(CORRECT_NAME, input, CORRECT_QUANTITY, CORRECT_PROMOTION);

        // when & then
        assertThatThrownBy(() -> new Product(productFileDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_NUMBER.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"콜라", "-10"})
    @DisplayName("상품 수량에 숫자가 아닌 값이나 음수가 들어오면 에러를 반환한다.")
    void 상품_수량에_숫자가_아닌_값이나_음수가_들어오면_에러를_반환한다(String input) {
        // given
        ProductFileDto productFileDto = new ProductFileDto(CORRECT_NAME, CORRECT_PRICE, input, CORRECT_PROMOTION);

        // when & then
        assertThatThrownBy(() -> new Product(productFileDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_NUMBER.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("프로모션 이름에 빈 문자열이나 공백이 들어오면 에러를 반환한다.")
    void 프로모션_이름에_빈_문자열이나_공백이_들어오면_에러를_반환한다(String input) {
        // given
        ProductFileDto productFileDto = new ProductFileDto(CORRECT_NAME, CORRECT_PRICE, CORRECT_QUANTITY, input);

        // when & then
        assertThatThrownBy(() -> new Product(productFileDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.EMPTY_NOT_ALLOWED.getMessage());
    }
}
