package exampleCode.score;

import static org.junit.jupiter.api.Assertions.*;

import exampleCode.domain.ScoreCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScoreCollectionTest {

    @Test
    public void test() {
//         fail("Not yet implemented");
    }

    @Test
    @DisplayName("두개 번호의 평균")
    public void answersArithmeticMeanOfTwoNumbers() {
        // 준비 given
        ScoreCollection collection = new ScoreCollection();
        collection.add(() -> 5);
        collection.add(() -> 7);

        // 실행 when
        int actualResult = collection.arithmeticMean();

        // 단언 then
        assertEquals(6, actualResult);
    }
}