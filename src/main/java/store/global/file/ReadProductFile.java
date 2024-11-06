package store.global.file;

import java.util.List;
import store.dto.ProductFileDto;

public class ReadProductFile extends ReadFile {
    private static final String DETAIL_DELIMITER = ",";

    public List<ProductFileDto> getProductDtos(String filePath) {
        List<String> allLines = super.getAllLines(filePath);
        return allLines.stream().map(this::parseLineToDto).toList();
    }

    private ProductFileDto parseLineToDto(String line) {
        String[] details = line.split(DETAIL_DELIMITER);
        return new ProductFileDto(details[0], details[1], details[2], details[3]);
    }
}
