package store.domain;

import java.util.List;
import store.global.file.dto.PromotionFileDto;

public class Promotions {
    private List<Promotion> promotions;

    public Promotions(List<PromotionFileDto> promotionFileDtos) {
        this.promotions = promotionFileDtos.stream().map(Promotion::new).toList();
    }

    public Promotion findPromotionByName(String promotionName) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equals(promotionName))
                .findFirst()
                .orElse(null);
    }
}
