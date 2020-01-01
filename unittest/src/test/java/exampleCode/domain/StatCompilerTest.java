package exampleCode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

class StatCompilerTest {

    @Test
    public void test() {
        StatCompiler stats = new StatCompiler();

        List<BooleanAnswer> answers = new ArrayList<>();
        answers.add(new BooleanAnswer(1, true));
        answers.add(new BooleanAnswer(1, true));
        answers.add(new BooleanAnswer(1, true));
        answers.add(new BooleanAnswer(1, false));
        answers.add(new BooleanAnswer(2, true));
        answers.add(new BooleanAnswer(2, true));

        Map<String, Map<Boolean,AtomicInteger>> responses = stats.responsesByQuestion(answers);

        assertThat(responses.get("Tuition reimbursement?").get(Boolean.TRUE).get()).isEqualTo(3);
        assertThat(responses.get("Tuition reimbursement?").get(Boolean.FALSE).get()).isEqualTo(1);
        assertThat(responses.get("Relocation package?").get(Boolean.TRUE).get()).isEqualTo(2);
        assertThat(responses.get("Relocation package?").get(Boolean.FALSE).get()).isEqualTo(0);
    }

    @Test
    public void responsesByQuestionAnswersCountsByQuestionText() {
        StatCompiler stats = new StatCompiler();
        List<BooleanAnswer> answers = new ArrayList<>();
        answers.add(new BooleanAnswer(1, true));
        answers.add(new BooleanAnswer(1, true));
        answers.add(new BooleanAnswer(1, true));
        answers.add(new BooleanAnswer(1, false));
        answers.add(new BooleanAnswer(2, true));
        answers.add(new BooleanAnswer(2, true));
        Map<Integer,String> questions = new HashMap<>();
        questions.put(1, "Tuition reimbursement?");
        questions.put(2, "Relocation package?");

        Map<String, Map<Boolean, AtomicInteger>> responses =
            stats.responsesByQuestion(answers, questions);

        assertThat(responses.get("Tuition reimbursement?").
            get(Boolean.TRUE).get()).isEqualTo(3);
        assertThat(responses.get("Tuition reimbursement?").
            get(Boolean.FALSE).get()).isEqualTo(1);
        assertThat(responses.get("Relocation package?").
            get(Boolean.TRUE).get()).isEqualTo(2);
        assertThat(responses.get("Relocation package?").
            get(Boolean.FALSE).get()).isEqualTo(0);
    }
}