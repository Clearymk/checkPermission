package util;

import java.util.HashSet;
import java.util.Set;

public class SetOP {
    public static Set<String> performUnion(Set<String> s1, Set<String> s2) {
        Set<String> s1Unions2 = new HashSet<>(s1);
        s1Unions2.addAll(s2);
        return s1Unions2;
    }

    public static Set<String> performIntersection(Set<String> s1, Set<String> s2) {
        Set<String> s1Intersections2 = new HashSet<>(s1);
        s1Intersections2.retainAll(s2);
        return s1Intersections2;
    }

    public static Set<String> performDifference(Set<String> s1, Set<String> s2) {
        Set<String> s1Differences2 = new HashSet<>(s1);
        s1Differences2.removeAll(s2);
        return s1Differences2;
    }
}
