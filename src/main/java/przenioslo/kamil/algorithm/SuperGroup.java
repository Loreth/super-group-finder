package przenioslo.kamil.algorithm;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.clique.PivotBronKerboschCliqueFinder;
import org.jgrapht.alg.interfaces.MaximalCliqueEnumerationAlgorithm;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static przenioslo.kamil.runner.Constants.*;

public class SuperGroup {
    private static ResourceBundle rb = ResourceBundle.getBundle("przenioslo.kamil.algorithm.SuperGroupStrings");
    private Map<String, Set<String>> personToFriends;

    public SuperGroup(Map<String, Set<String>> personToFriends) {
        this.personToFriends = personToFriends;
    }

    public Set<String> getLargestSuperGroup() {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        Graphs.addAllVertices(graph, personToFriends.keySet());
        personToFriends.forEach((person, friends) -> friends.forEach(friend -> graph.addEdge(person, friend)));

        PivotBronKerboschCliqueFinder<String, DefaultEdge> cliqueFinder = new PivotBronKerboschCliqueFinder<>(graph);

        return findBestSuperGroup(cliqueFinder);
    }

    public static Map<String, Set<String>> getPersonToFriendsMappingFromStdin() {
        Scanner scanner = new Scanner(System.in);
        Map<String, Set<String>> personToFriends;

        System.out.println(rb.getString("promptMsg"));
        String[] peopleAndRelationsCount = scanner.nextLine().split(INPUT_DELIMITER);
        if (peopleAndRelationsCount.length != 2) {
            System.out.println(rb.getString("tooFewInitialNumbersError"));
            return Collections.emptyMap();
        }

        int peopleCount = Integer.parseInt(peopleAndRelationsCount[0]);
        int relationsCount = Integer.parseInt(peopleAndRelationsCount[1]);


        if (verifyInitialNumbers(peopleCount, relationsCount)) {
            personToFriends = addRelationsFromStdin(scanner, relationsCount);
        } else {
            return Collections.emptyMap();
        }
        return personToFriends;
    }

    private static boolean verifyInitialNumbers(int peopleCount, int relationsCount) {
        if (peopleCount < MIN_PEOPLE_COUNT || peopleCount > MAX_PEOPLE_COUNT) {
            System.out.println(rb.getString("peopleCountOutOfBounds"));
            return false;
        } else if (relationsCount < MIN_RELATIONS_COUNT || relationsCount > MAX_RELATIONS_COUNT) {
            System.out.println(rb.getString("relationsCountOutOfBounds"));
            return false;
        }
        return true;
    }

    private static Map<String, Set<String>> addRelationsFromStdin(Scanner scanner, int relationsToRead) {
        Map<String, Set<String>> personToFriends = new HashMap<>();
        while (relationsToRead-- > 0 && scanner.hasNextLine()) {
            String[] relation = scanner.nextLine().split(INPUT_DELIMITER);
            if (relation.length != 2) {
                System.out.println(rb.getString("incompleteRelationError"));
                return Collections.emptyMap();
            }

            personToFriends.computeIfAbsent(relation[0], key -> new HashSet<>()).add(relation[1]);
            personToFriends.computeIfAbsent(relation[1], key -> new HashSet<>()).add(relation[0]);
        }

        return personToFriends;
    }

    private Set<String> findBestSuperGroup(MaximalCliqueEnumerationAlgorithm<String, DefaultEdge> maxCliqueFinder) {
        List<SortedSet<String>> sortedMaxCliques = getSortedSets(maxCliqueFinder);
        List<Long> friendsOutsideOfCliqueCount = getFriendsOutsideOfCliqueCount(sortedMaxCliques);

        Map<SortedSet<String>, Long> cliqueToOutsideFriendsCount = IntStream
                .range(0, sortedMaxCliques.size())
                .boxed()
                .collect(Collectors.toMap(sortedMaxCliques::get, friendsOutsideOfCliqueCount::get));

        long maxOutsideFriends = Collections.max(friendsOutsideOfCliqueCount);

        return getCliqueToOutsideFriendsCount(cliqueToOutsideFriendsCount, maxOutsideFriends);
    }

    private Set<String> getCliqueToOutsideFriendsCount(Map<SortedSet<String>, Long> cliqueToOutsideFriendsCount, long maxOutsideFriends) {
        return cliqueToOutsideFriendsCount
                .entrySet()
                .stream()
                .filter(entry -> entry
                        .getValue()
                        .equals(maxOutsideFriends))
                .map(Map.Entry::getKey)
                .sorted(getLexicographicSetComparator())
                .collect(Collectors.toList())
                .get(0);
    }

    private Comparator<SortedSet<String>> getLexicographicSetComparator() {
        return (set1, set2) -> {
            Iterator<String> iteratorSet1 = set1.iterator();
            Iterator<String> iteratorSet2 = set2.iterator();
            while (iteratorSet1.hasNext() && iteratorSet2.hasNext()) {
                int comparison = iteratorSet1.next().compareTo(iteratorSet2.next());
                if (comparison != 0) {
                    return comparison;
                }
            }

            return 0;
        };
    }

    private List<Long> getFriendsOutsideOfCliqueCount(List<SortedSet<String>> sortedMaxCliques) {
        List<Long> friendsOutsideOfCliqueCount = new ArrayList<>();

        for (Set<String> clique : sortedMaxCliques) {
            friendsOutsideOfCliqueCount.add(personToFriends
                    .keySet()
                    .stream()
                    .filter(clique::contains)
                    .flatMap(excluded -> personToFriends.get(excluded).stream())
                    .filter(clique::contains)
                    .count());
        }
        return friendsOutsideOfCliqueCount;
    }

    private List<SortedSet<String>> getSortedSets(MaximalCliqueEnumerationAlgorithm<String, DefaultEdge> maxCliqueFinder) {
        List<SortedSet<String>> sortedMaxCliques = new ArrayList<>();

        maxCliqueFinder
                .iterator()
                .forEachRemaining(clique -> sortedMaxCliques.add(new TreeSet<>(clique)));

        return sortedMaxCliques;
    }
}
