package chapter13;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileMatcher_4Test {
    private BooleanQuestion question;
    private Criteria criteria;
    private ProfileMatcher_4 matcher;
    private Profile matchingProfile;
    private Profile nonMatchingProfile;

    @Before
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

    @Before
    public void createMatcher() {
        matcher = new ProfileMatcher_4();
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

    @Before
    public void createMatchListener() {
        listener = mock(MatchListener.class);
    }

    @Test
    public void processNotifiesListenerOnMatch() {
        matcher.add(matchingProfile);
        MatchSet set = matchingProfile.getMatchSet(criteria);

        matcher.process(listener, set);

        verify(listener).foundMatch(matchingProfile, set);
    }

    @Test
    public void processDoesNotNotifyListenerWhenNoMatch() {
        matcher.add(nonMatchingProfile);
        MatchSet set = nonMatchingProfile.getMatchSet(criteria);

        matcher.process(listener, set);

        verify(listener, never()).foundMatch(nonMatchingProfile, set);
    }

    @Test
    public void gathersMatchingProfiles() {
        Set<String> processedSets = Collections.synchronizedSet(new HashSet<>()); // (1)
        BiConsumer<MatchListener, MatchSet> processFunction =
                (listener, set) -> { // (2)
                    processedSets.add(set.getProfileId()); // (3)
                };
        List<MatchSet> matchSets = createMatchSets(100); // (4)

        matcher.findMatchingProfiles( // (5)
                criteria, listener, matchSets, processFunction);

        while (!matcher.getExecutor().isTerminated()); // (6)

        assertThat(processedSets, equalTo(matchSets.stream()
                .map(MatchSet::getProfileId).collect(Collectors.toSet()))); // (7)
    }

    private List<MatchSet> createMatchSets(int count) {
        List<MatchSet> sets = new ArrayList<>();
        for (int i = 0; i < count; i++)
            sets.add(new MatchSet(String.valueOf(i), null, null));
        return sets;
    }

    private Answer matchingAnswer() {
        return new Answer(question, Bool.TRUE);
    }

    private Answer nonMatchingAnswer() {
        return new Answer(question, Bool.FALSE);
    }
}