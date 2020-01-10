package chapter8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * 1. 메서드 추출 : matches
 * 2. 메서드 이동 : matches Answers -> Criteria
 * 3. answerMatching 메서드 생성, answer 변수 제거 및 인라인
 *
 * */
public class Profile_Before {
    private Map<String,Answer> answers = new HashMap<>();
    // ...

    private int score;
    private String name;

    public Profile_Before(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(Answer answer) {
        answers.put(answer.getQuestionText(), answer);
    }

    public boolean matches(Criteria criteria) {
        score = 0;

        boolean kill = false;
        boolean anyMatches = false;
        for (Criterion criterion: criteria) {
//          3. answerMatching 메서드 생성, answer 변수 제거 및 인라인
            Answer answer = answers.get(
                    criterion.getAnswer().getQuestionText());

//            1. 메서드 추출 : matches
            boolean match = criterion.getWeight() == Weight.DontCare ||
                            answer.match(criterion.getAnswer());
            if (!match && criterion.getWeight() == Weight.MustMatch) {
                kill = true;
            }
            if (match) {
                score += criterion.getWeight().getValue();
            }
            anyMatches |= match;
            // ...
        }
        if (kill)
            return false;
        return anyMatches;
    }

    public int score() {
        return score;
    }

    public List<Answer> classicFind(Predicate<Answer> pred) {
        List<Answer> results = new ArrayList<Answer>();
        for (Answer answer: answers.values())
            if (pred.test(answer))
                results.add(answer);
        return results;
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Answer> find(Predicate<Answer> pred) {
        return answers.values().stream()
                .filter(pred)
                .collect(Collectors.toList());
    }
}
