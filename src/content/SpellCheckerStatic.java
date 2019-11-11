package content;

import java.util.Collections;
import java.util.List;

// Inappropriate use of static utility - inflexible and untestable!
public class SpellCheckerStatic {
    private static final Lexicon dictionary = new Lexicon();

    private SpellCheckerStatic() {} // Noninstantiable

    public static boolean isValid(String word){
        return true;
    };

    public static List<String> suggestions(String typo) {
        return Collections.emptyList();
    }
}
