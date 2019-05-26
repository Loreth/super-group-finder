package przenioslo.kamil.runner;


import przenioslo.kamil.algorithm.SuperGroup;

import java.util.Set;

import static przenioslo.kamil.runner.Constants.OUTPUT_DELIMITER;

public class Runner {
    public static void main(String[] args) {
        SuperGroup superGroup = new SuperGroup(SuperGroup.getPersonToFriendsMappingFromStdin());
        Set<String> largestSuperGroup = superGroup.getLargestSuperGroup();
        System.out.println(largestSuperGroup.size());
        System.out.println(String.join(OUTPUT_DELIMITER, largestSuperGroup));
    }
}
