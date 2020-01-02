package chapter7;

import exampleCode.domain.BearingOutOfRangeException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BearingTest {
    @Test
    public void throwsOnNegativeNumber() {
        assertThrows(BearingOutOfRangeException.class, () -> new Bearing(-1));

    }

    @Test
    public void throwsWhenBearingTooLarge() {
        assertThrows(BearingOutOfRangeException.class, () -> new Bearing(Bearing.MAX + 1));
    }

    @Test
    public void answersValidBearing() {
        assertThat(new Bearing(Bearing.MAX).value(), equalTo(Bearing.MAX));
    }

    @Test
    public void answersAngleBetweenItAndAnotherBearing() {
        assertThat(new Bearing(15).angleBetween(new Bearing(12)), equalTo(3));
    }

    @Test
    public void angleBetweenIsNegativeWhenThisBearingSmaller() {
        assertThat(new Bearing(12).angleBetween(new Bearing(15)), equalTo(-3));
    }

}