package store.global.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import store.global.constants.ErrorMessage;
import store.global.exception.CustomException;

public class ReadFile {
    public static List<String> getAllLines(String filePath) {
        try {
            List<String> allLines = Files.readAllLines(Paths.get(filePath));
            validateFileEmpty(allLines);
            return allLines;
        } catch (IOException e) {
            throw CustomException.of(ErrorMessage.FILE_NOT_FOUND);
        }
    }

    private static void validateFileEmpty(List<String> allLines) {
        if (allLines.isEmpty()) {
            throw CustomException.of(ErrorMessage.FILE_EMPTY);
        }
    }
}
