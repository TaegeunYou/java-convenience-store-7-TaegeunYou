package store.global.file;

import java.util.List;
import store.global.constants.ErrorMessage;
import store.global.exception.CustomException;

public class ReadProductFile extends ReadFile {
    private static final String DETAIL_DELIMITER = ",";
    private static final int DETAIL_COUNT = 4;

    public List<ProductFileDto> getProductDtos(String filePath) {
        List<String> allLines = super.getAllLines(filePath);
        return allLines.stream().map(this::parseLineToDto).toList();
    }

    private ProductFileDto parseLineToDto(String line) {
        String[] details = line.split(DETAIL_DELIMITER);
        validateDetailsSize(details);
        return new ProductFileDto(details[0], details[1], details[2], details[3]);
    }

    private void validateDetailsSize(String[] details) {
        if (details.length != DETAIL_COUNT) {
            throw CustomException.of(ErrorMessage.INVALID_PRODUCT_DETAIL_COUNT);
        }
    }
}
