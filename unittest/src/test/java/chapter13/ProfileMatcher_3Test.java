package chapter13;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.ExpectedCount.never;

public class ProfileMatcher_3Test {
    private BooleanQuestion question;
    private Criteria criteria;
    private ProfileMatcher_3 matcher;
    private Profile matchingProfile;
    private Profile nonMatchingProfile;

    @BeforeEach
    public void create() {
        question = new BooleanQuestion(1, "");
        criteria = new Criteria();
        criteria.add(new Criterion(matchingAnswer(), Weight.MustMatch));
        matchingProfile = createMatchingProfile("matching");
        nonMatchingProfile = createNonMatchingProfile("nonMatching");
    }

    private Profile createMatchingProfile(String name) {
        Profile profile = new Profile(name);
        profile.add(matchingAnswer());
        return profile;
    }

    private Profile createNonMatchingProfile(String name) {
        Profile profile = new Profile(name);
        profile.add(nonMatchingAnswer());
        return profile;
    }

    @BeforeEach
    public void createMatcher() {
        matcher = new ProfileMatcher_3();
    }

    @Test
    public void collectsMatchSets() {
        matcher.add(matchingProfile);
        matcher.add(nonMatchingProfile);

        List<MatchSet> sets = matcher.collectMatchSets(criteria);

        assertThat(sets.stream().map(set -> set.getProfileId()).collect(Collectors.toSet()),
                equalTo(new HashSet<>(Arrays.asList(matchingProfile.getId(), nonMatchingProfile.getId()))));
    }

    private MatchListener listener;

    @BeforeEach
    public void createMatchListener() {
        listener = mock(MatchListener.class); // (1) MatchListener 목 인스턴스 생성
    }

    @Test
    public void processNotifiesListenerOnMatch() {
        matcher.add(matchingProfile);  // (2)매칭되는 프로파일 mater 변수에 추가
        MatchSet set = matchingProfile.getMatchSet(criteria); // (3) 조건에 매칭되는 프로파일 MatchSet 객체 요청

        matcher.process(listener, set); // (4)

        verify(listener).foundMatch(matchingProfile, set); // (5) 목 리스너 객체에 foundMatch() 메서드 호출 확인
    }

    @Test
    public void processDoesNotNotifyListenerWhenNoMatch() {
        matcher.add(nonMatchingProfile);
        MatchSet set = nonMatchingProfile.getMatchSet(criteria);

        matcher.process(listener, set);

        verify(listener, Mockito.never()).foundMatch(nonMatchingProfile, set);
    }

    private Answer matchingAnswer() {
        return new Answer(question, Bool.TRUE);
    }

    private Answer nonMatchingAnswer() {
        return new Answer(question, Bool.FALSE);
    }
}
