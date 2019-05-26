package przenioslo.kamil.algorithm;

import java.util.ListResourceBundle;

public class SuperGroupStrings extends ListResourceBundle {
    protected Object[][] getContents() {
        return contents;
    }

    private static final Object[][] contents = {
            {"promptMsg", "Podaj dane wejściowe: "},
            {"tooFewInitialNumbersError", "Nie podano liczby osób oraz liczby relacji między nimi"},
            {"incompleteRelationError", "Podano niekompletną relację"},
            {"peopleCountOutOfBounds", "Podana liczba osób znajduje się poza przyjętymi granicami"},
            {"relationsCountOutOfBounds", "Podana liczba relacji znajduje się poza przyjętymi granicami"}
    };
}
