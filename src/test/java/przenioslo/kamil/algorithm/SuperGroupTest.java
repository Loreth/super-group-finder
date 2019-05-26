package przenioslo.kamil.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SuperGroupTest {
    private Map<String, Set<String>> correctPersonToFriends;

    @BeforeEach
    void setUp() {
        correctPersonToFriends = new HashMap<>();
        correctPersonToFriends.put("Mirek", new TreeSet<>(Arrays.asList("Ola", "Ala", "Ela")));
        correctPersonToFriends.put("Ola", new TreeSet<>(Arrays.asList("Mirek", "Ala")));
        correctPersonToFriends.put("Ala", new TreeSet<>(Arrays.asList("Mirek", "Kasia", "Ola", "Janek")));
        correctPersonToFriends.put("Ela", new TreeSet<>(Arrays.asList("Mirek")));
        correctPersonToFriends.put("Kasia", new TreeSet<>(Arrays.asList("Ala", "Janek")));
        correctPersonToFriends.put("Janek", new TreeSet<>(Arrays.asList("Ala", "Kasia", "Zosia")));
        correctPersonToFriends.put("Zosia", new TreeSet<>(Arrays.asList("Janek")));
    }

    @Test
    void assertCorrectSuperGroupIsFound() {
        SuperGroup superGroup = new SuperGroup(correctPersonToFriends);
        Set<String> correctBestSuperGroup = new TreeSet<>(Arrays.asList("Ala", "Janek", "Kasia"));
        assertEquals(correctBestSuperGroup, superGroup.getLargestSuperGroup());
    }
}