package content;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

// Dependency injection provides flexibility and testability
public class SpellCheckerInjection {
    private final Lexicon dictionary;

    public SpellCheckerInjection(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public boolean isValid(String word) { return true; }

    public List<String> suggestions(String typo) { return Collections.emptyList(); }
}
