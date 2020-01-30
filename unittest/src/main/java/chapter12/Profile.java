package chapter12;

import chapter2.Answer;
import chapter2.Criterion;

public class Profile {
    private Answer answer;

    public boolean matches(Criterion criterion) {
        return answer != null && answer.match(criterion.getAnswer());
    }

    public void add(Answer answer) {
        this.answer = answer;
    }
}
