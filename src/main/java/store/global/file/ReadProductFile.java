package store.global.file;

import java.util.List;
import store.global.file.dto.ProductFileDto;

public class ReadProductFile extends ReadFile {
    private static final int DETAIL_COUNT = 4;

    public List<ProductFileDto> getProductDtos(String filePath) {
        List<String> allLines = super.getAllLines(filePath);
        return allLines.stream().map(this::parseLineToDto).toList();
    }

    private ProductFileDto parseLineToDto(String line) {
        String[] details = line.split(DETAIL_DELIMITER);
        validateDetailsSize(details, DETAIL_COUNT);

        return new ProductFileDto(details[0], details[1], details[2], parsePromotion(details[3]));
    }

    private String parsePromotion(String promotion) {
        if (promotion.equals("null")) {
            return null;
        }
        return promotion;
    }
}
