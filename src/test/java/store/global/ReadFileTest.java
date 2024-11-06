package store.global;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.global.file.ReadFile;

class ReadFileTest {

    @Test
    void getAllLines() {
        // given
        String filePath = "src/main/resources/products.md";

        // when
        List<String> allLines = ReadFile.getAllLines(filePath);

        // then
        Assertions.assertThat(allLines.getFirst()).isEqualTo("name,price,quantity,promotion");
        Assertions.assertThat(allLines.size()).isEqualTo(17);
    }
}