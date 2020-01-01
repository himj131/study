package chapter6;

import exampleCode.domain.ScoreCollection;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.Math.abs;
import static org.hamcrest.CoreMatchers.equalTo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Chapter6Test {

    private ScoreCollection collection;

    @BeforeEach
    void setUp() {
        collection = new ScoreCollection();
    }

    @Test
    void throwsExceptionWhenAddingNull() {
        assertThrows(IllegalArgumentException.class, () -> collection.add(null));
    }


    @Test
    public void answersZeroWhenNoElementsAdded() {
        assertThat(collection.arithmeticMean(), equalTo(0));
    }

    /**
     * 1.long 타입으로 변경 (125p)
     * */
    @Test
    public void dealsWithIntegerOverflow() {
        collection.add(() -> Integer.MAX_VALUE);
        collection.add(() -> 1);

        assertThat(collection.arithmeticMean(), equalTo(1073741824));
    }

    /**
     * 2.오버플로우 발생 허용 테스트 -> 예외를 던지는 것이 좋다. (125p)
     * */
    @Test
    public void doesNotProperlyHandleIntegerOverflow() {
        collection.add(() -> Integer.MAX_VALUE);
        collection.add(() -> 1);

        assertTrue(collection.arithmeticMean() < 0);
    }
    
    /**
     * 6.4 NewtonTest
     * */
    static class Newton {
        private static final double TOLERANCE = 1E-16;

        public static double squareRoot(double n) {
            double approx = n;
            while (abs(approx - n / approx) > TOLERANCE * approx)
                approx = (n / approx + approx) / 2.0;
            return approx;
        }
    }

    @Test
    public void squareRoot() {
        double result = Newton.squareRoot(250.0);
        Assertions.assertThat(result * result).isCloseTo(250.0, Offset.offset(Newton.TOLERANCE));
    }

//    @Test
//    public void squareRootVerifiedUsingLibrary() {
//        Assertions.assertThat(Newton.squareRoot(1969.0)).isCloseTo(Math.sqrt(1969.0), Offset.offset(Newton.TOLERANCE));
//    }

}
