package store.domain;

import java.util.List;
import store.global.file.dto.PromotionFileDto;

public class Promotions {
    List<Promotion> promotions;
    public Promotions(List<PromotionFileDto> promotionFileDtos) {
        this.promotions = promotionFileDtos.stream().map(Promotion::new).toList();
    }
}
