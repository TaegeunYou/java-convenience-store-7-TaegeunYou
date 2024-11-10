package store.global;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.global.constants.FilePath;
import store.global.file.ReadProductFile;

class ReadFileTest {

    @DisplayName("파일을 읽어서 모든 줄을 정상적으로 가져오는지 확인한다.")
    @Test
    void getAllLinesTest() {
        // given
        ReadProductFile readProductFile = new ReadProductFile();

        // when
        List<String> allLines = readProductFile.getAllLines(FilePath.PRODUCTS.getPath());

        // then
        Assertions.assertThat(allLines.getFirst()).isEqualTo("콜라,1000,10,탄산2+1");
        Assertions.assertThat(allLines.size()).isEqualTo(16);
    }
}