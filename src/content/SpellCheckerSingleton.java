package content;

import java.util.Collections;
import java.util.List;

public class SpellCheckerSingleton {
    private final Lexicon dictionary = new Lexicon();

    private SpellCheckerSingleton() {} // Noninstantiable
    public static SpellCheckerSingleton INSTANCE = new SpellCheckerSingleton();

    public boolean isValid(String word) {
        return true;
    }

    public List<String> suggestions(String typo) {
        return Collections.emptyList();
    }
}