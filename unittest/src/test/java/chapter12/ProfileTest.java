package chapter12;

import chapter2.Bool;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {
    @Test
    void matchesNothingWhenProfileEmpty() {
        Profile profile = new Profile();

        Question question = new BooleanQuestion(1, "Relocation package?");
        Criterion criterion = new Creterion(new Answer(question, Bool.TRUE), Weight.DontCar);

        boolean result = profile.matches(criterion);
        assertFalse(result);

    }
}