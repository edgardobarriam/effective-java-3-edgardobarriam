package content;

// Noninstantiable utility class
public class UtilityClass {

    // Supress default constructor for noninstantiability
    private UtilityClass() {
        throw new AssertionError(); // The assertion error isn't strictly required, but it provides insurance
        // in case the constructor is accidentally invoked from within the class. It guarantees the class will never be
        // instantiated under any circumstances.
    }

    public static void doSomething() {
        System.out.println("Doing something");
    }
}
