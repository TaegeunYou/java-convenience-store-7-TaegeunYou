package store.global;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.global.constants.FilePath;
import store.global.file.ReadFile;
import store.global.file.ReadProductFile;

class ReadFileTest {

    @Test
    void getAllLines() {
        // given
        ReadProductFile readProductFile = new ReadProductFile();

        // when
        List<String> allLines = readProductFile.getAllLines(FilePath.PRODUCTS.getPath());

        // then
        Assertions.assertThat(allLines.getFirst()).isEqualTo("name,price,quantity,promotion");
        Assertions.assertThat(allLines.size()).isEqualTo(17);
    }
}