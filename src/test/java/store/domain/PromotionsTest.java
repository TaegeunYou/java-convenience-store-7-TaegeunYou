package store.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.global.file.dto.PromotionFileDto;

public class PromotionsTest {

    private Promotions promotions;

    @BeforeEach
    void setUp() {
        PromotionFileDto promoDto1 = new PromotionFileDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31");
        PromotionFileDto promoDto2 = new PromotionFileDto("MD추천상품", "1", "1", "2024-01-01", "2024-12-31");

        promotions = new Promotions(List.of(promoDto1, promoDto2));
    }

    @Test
    @DisplayName("프로모션 이름으로 Promotion을 찾아 반환한다.")
    void 프로모션_이름으로_Promotion_찾기() {
        // when
        Promotion promotion = promotions.findPromotionByName("탄산2+1");

        // then
        assertThat(promotion).isNotNull();
        assertThat(promotion.getName()).isEqualTo("탄산2+1");
        assertThat(promotion.getBuy()).isEqualTo(2);
        assertThat(promotion.getGet()).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 프로모션 이름으로 검색하면 null을 반환한다.")
    void 존재하지_않는_프로모션_이름으로_검색시_null_반환() {
        // when
        Promotion promotion = promotions.findPromotionByName("없는프로모션");

        // then
        assertThat(promotion).isNull();
    }

    @Test
    @DisplayName("프로모션 목록이 Promotions 객체에 올바르게 추가되었는지 확인한다.")
    void 프로모션_목록_추가_확인() {
        // when
        Promotion promotion1 = promotions.findPromotionByName("탄산2+1");
        Promotion promotion2 = promotions.findPromotionByName("MD추천상품");

        // then
        assertThat(promotion1).isNotNull();
        assertThat(promotion2).isNotNull();
        assertThat(promotion1.getName()).isEqualTo("탄산2+1");
        assertThat(promotion2.getName()).isEqualTo("MD추천상품");
    }
}
