package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.global.constants.ErrorMessage;
import store.global.file.dto.PromotionFileDto;

public class PromotionTest {
    private static final String CORRECT_NAME = "탄산2+1";
    private static final String CORRECT_BUY = "2";
    private static final String CORRECT_GET = "1";
    private static final String CORRECT_START_DATE = "2024-01-01";
    private static final String CORRECT_END_DATE = "2024-12-31";

    @ParameterizedTest
    @CsvSource(value = {
            "탄산2+1,2,1,2024-01-01,2024-12-31",
            "MD추천상품,1,1,2024-01-01,2024-12-31",
            "반짝할인,1,1,2024-11-01,2024-11-30"
    }, delimiter = ',')
    @DisplayName("프로모션을 정상적으로 생성하는지 확인한다.")
    void 프로모션을_정상적으로_생성하는지_확인한다(String name, String buy, String get, String startDate, String endDate) {
        // given
        PromotionFileDto productFileDto = new PromotionFileDto(name, buy, get, startDate, endDate);

        // when
        Promotion promotion = new Promotion(productFileDto);

        // then
        assertThat(promotion.getName()).isEqualTo(name);
        assertThat(promotion.getBuy()).isEqualTo(Integer.parseInt(buy));
        assertThat(promotion.getGet()).isEqualTo(Integer.parseInt(get));
        assertThat(promotion.getStartDate()).isEqualTo(startDate);
        assertThat(promotion.getEndDate()).isEqualTo(endDate);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("프로모션 이름에 빈 문자열이나 공백이 들어오면 에러를 반환한다.")
    void 프로모션_이름에_빈_문자열이나_공백이_들어오면_에러를_반환한다(String input) {
        // given
        PromotionFileDto promotionFileDto = new PromotionFileDto(
                input, CORRECT_BUY, CORRECT_GET, CORRECT_START_DATE, CORRECT_END_DATE
        );

        // when & then
        assertThatThrownBy(() -> new Promotion(promotionFileDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.EMPTY_NOT_ALLOWED.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"콜라", "-1000"})
    @DisplayName("프로모션 조건에 숫자가 아닌 값이나 음수가 들어오면 에러를 반환한다.")
    void 프로모션_조건에_숫자가_아닌_값이나_음수가_들어오면_에러를_반환한다(String input) {
        // given
        PromotionFileDto promotionFileDto = new PromotionFileDto(
                CORRECT_NAME, input, CORRECT_GET, CORRECT_START_DATE, CORRECT_END_DATE
        );

        // when & then
        assertThatThrownBy(() -> new Promotion(promotionFileDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_NUMBER.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"콜라", "-10"})
    @DisplayName("프로모션 증정에 숫자가 아닌 값이나 음수가 들어오면 에러를 반환한다.")
    void 프로모션_증정에_숫자가_아닌_값이나_음수가_들어오면_에러를_반환한다(String input) {
        // given
        PromotionFileDto promotionFileDto = new PromotionFileDto(
                CORRECT_NAME, CORRECT_BUY, input, CORRECT_START_DATE, CORRECT_END_DATE
        );

        // when & then
        assertThatThrownBy(() -> new Promotion(promotionFileDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_NUMBER.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024", "2024-01-01T09:31:29"})
    @DisplayName("프로모션 시작 날짜가 올바른 날짜 형식이 아니면 에러를 반환한다.")
    void 프로모션_시작_날짜가_올바른_날짜_형식이_아니면_에러를_반환한다(String input) {
        // given
        PromotionFileDto promotionFileDto = new PromotionFileDto(
                CORRECT_NAME, CORRECT_BUY, CORRECT_GET, input, CORRECT_END_DATE
        );

        // when & then
        assertThatThrownBy(() -> new Promotion(promotionFileDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_DATE.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024", "2024-12-31T09:31:29"})
    @DisplayName("프로모션 종료 날짜가 올바른 날짜 형식이 아니면 에러를 반환한다.")
    void 프로모션_종료_날짜가_올바른_날짜_형식이_아니면_에러를_반환한다(String input) {
        // given
        PromotionFileDto promotionFileDto = new PromotionFileDto(
                CORRECT_NAME, CORRECT_BUY, CORRECT_GET, CORRECT_START_DATE, input
        );

        // when & then
        assertThatThrownBy(() -> new Promotion(promotionFileDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_DATE.getMessage());
    }

}
