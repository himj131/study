package chapter7;

import chapter3.ExpectToFail;
import org.junit.After;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import static chapter7.ConstrainsSidesTo.constrainsSidesTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class RectangleTest {
    private Rectangle rectangle;

    @After
    public void ensureInvariant() {
        assertThat(rectangle, constrainsSidesTo(100));
    }

    @Test
    public void answersArea() {
        rectangle = new Rectangle(new Point(5, 5), new Point (15, 10));
        assertThat(rectangle.area(), equalTo(50));
    }

    @Ignore
    @ExpectToFail
    @Test
    public void allowsDynamicallyChangingSize() {
        rectangle = new Rectangle(new Point(5, 5));
        rectangle.setOppositeCorner(new Point(130, 130));
        assertThat(rectangle.area(), equalTo(15625));
    }
}
