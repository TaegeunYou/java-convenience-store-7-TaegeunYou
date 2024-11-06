package store.global.file;

import java.util.List;

public class ReadPromotionFile extends ReadFile {
    private static final int DETAIL_COUNT = 5;

    public List<PromotionFileDto> getPromotionsDtos(String filePath) {
        List<String> allLines = super.getAllLines(filePath);
        return allLines.stream().map(this::parseLineToDto).toList();
    }

    private PromotionFileDto parseLineToDto(String line) {
        String[] details = line.split(DETAIL_DELIMITER);
        validateDetailsSize(details, DETAIL_COUNT);
        return new PromotionFileDto(details[0], details[1], details[2], details[3], details[4]);
    }
}
