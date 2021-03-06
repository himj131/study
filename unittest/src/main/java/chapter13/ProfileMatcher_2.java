package chapter13;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class ProfileMatcher_2 {
    private Map<String, Profile> profiles = new HashMap<>();
    private static final int DEFAULT_POOL_SIZE = 4;

    public void add(Profile profile) {
        profiles.put(profile.getId(), profile);
    }

    public void findMatchingProfiles(
            Criteria criteria, MatchListener listener) {
        ExecutorService executor =
                Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);

        for (MatchSet set: collectMatchSets(criteria)) {
            Runnable runnable = () -> {
                if (set.matches())
                    listener.foundMatch(profiles.get(set.getProfileId()), set);
            };
            executor.execute(runnable);
        }
        executor.shutdown();
    }

    List<MatchSet> collectMatchSets(Criteria criteria) {
        List<MatchSet> matchSets = profiles.values().stream()
                .map(profile -> profile.getMatchSet(criteria))
                .collect(Collectors.toList());
        return matchSets;
    }
}
