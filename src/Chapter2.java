import java.math.BigInteger;
import java.util.EnumSet;
import java.util.Random;

public class Chapter2 {

   void item1() { // Consider static factory methods instead of constructors

    // A class can provide a public static factory method. An example from Boolean:
    Boolean myBoolean = Boolean.valueOf(true);

    // (1) One advantage of static factory methods is that they have names, hence they are often more readable than constructors.
    BigInteger bigInteger = new BigInteger(1, 1, new Random());
    bigInteger = BigInteger.probablePrime(1, new Random()); // This is easier to read.

    // In cases where a class seems to require multiple constructors with the same signature, replace the constructors
    // with static factory methods and carefully chosen names to highlight their differences.

    // (2) Static factory methods are not necessarily required to create a new object each time they are invoked.
    Boolean anotherBoolean = Boolean.valueOf(false); // Boolean.valueOf() Never creates an object

    // (3) Static factory methods can return an object of any subtype of their return type.
    // (See java.util.Collections) (noninstsantiable companion classes were useful prior to Java 8)

    // (4) Static factories return class (subtypes) can vary from call to call depending on the input parameters.
    EnumSet<Color> enumSet = EnumSet.allOf(Color.class); // May return RegularEnumSet or JumboEnumSet.
    // Also hides implementation classes from clients, which can be helpful if there are any future changes on the implementation classes .

     // (5) Another advantage of static factories is that the class of the returned object need not exist when the class
     // containing the method is written.

     // (6) The main limitation of providing only static factory methods is that classes without public or protected
     // constructors cannot be subclassed.

     // (7) Also, static factory methods are hard for programmers to find.
     // This problem can be reduced by using common naming conventions. Some examples:
     // from(), of(), valueOf(), instance() or getInstance(), create() or newInstance(), get"Type"(), new"Type"(), "type"()

     // Static factories and public constructors both have their uses. Consider static factories instead of public constructors.
  }

  enum Color {
    RED, YELLOW, GREEN, BLUE, BLACK, WHITE
  }
}

