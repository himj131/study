package chapter8;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 1. 메서드 추출 : matches
 * 2. 메서드 이동 : matches Aswers -> Criteria
 * 3. answerMatching 메서드 생성, answer 변수 제거 및 인라인
 *
 * */
public class Profile_Ater {
    private Map<String, Answer> answers = new HashMap<>();
    private int score;
    private String name;

    public Profile_Ater(String name) {
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
        for (Criterion criterion : criteria) {

//            2. 메서드 이동 : matches Answers -> Criteria
//            3. answerMatching 메서드 생성, answer 변수 제거 및 인라인
            boolean match = criterion.matches(answerMatching(criterion));

            if (!match && criterion.getWeight() == Weight.MustMatch) {
                kill = true;
            }
            if (match) {
                score += criterion.getWeight().getValue();
            }
        }
        if (kill)
            return false;

        return anyMatches(criteria);
    }

    private boolean anyMatches(Criteria criteria) {
        boolean anyMatches = false;
        for (Criterion criterion : criteria)
            anyMatches |= criterion.matches(answerMatching(criterion));
        return anyMatches;
    }

    private Answer answerMatching(Criterion criterion) {
        return answers.get(criterion.getAnswer().getQuestionText());
    }

    public int score() {
        return score;
    }


//    1. 메서드 추출 : matches
//    private boolean matches(Criterion criterion, Answer answer) {
//        return criterion.getWeight() == Weight.DontCare ||
//                answer.match(criterion.getAnswer());
//    }

}