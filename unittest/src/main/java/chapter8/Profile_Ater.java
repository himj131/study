package chapter8;

import java.util.HashMap;
import java.util.Map;

public class Profile_Ater {
    private Map<String,Answer> answers = new HashMap<>();
    private int score;
//    private String name;
//
//    public Profile_Ater(String name) {
//        this.name = name;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void add(Answer answer) {
//        answers.put(answer.getQuestionText(), answer);
//    }

    public boolean matches(Criteria criteria) {
        score = 0;

        boolean kill = false;
        boolean anyMatches = false;
        for (Criterion criterion: criteria) {
            Answer answer = answers.get(
                    criterion.getAnswer().getQuestionText());
            boolean match = matches(criterion, answer);
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

    private boolean matches(Criterion criterion, Answer answer) {
        return criterion.getWeight() == Weight.DontCare ||
                answer.match(criterion.getAnswer());
    }

//    public int score() {
//        return score;
//    }
//
//    public List<Answer> classicFind(Predicate<Answer> pred) {
//        List<Answer> results = new ArrayList<Answer>();
//        for (Answer answer: answers.values())
//            if (pred.test(answer))
//                results.add(answer);
//        return results;
//    }
//
//    @Override
//    public String toString() {
//        return name;
//    }
//
//    public List<Answer> find(Predicate<Answer> pred) {
//        return answers.values().stream()
//                .filter(pred)
//                .collect(Collectors.toList());
//    }
}
