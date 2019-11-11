import content.*;

import java.math.BigInteger;
import java.util.EnumSet;
import java.util.Random;

import static content.NyPizza.Size.SMALL;
import static content.Pizza.Topping.*;

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
    // (See java.util.Collections) (non-instantiable companion classes were useful prior to Java 8)

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

  void item2() { // Consider a builder when faced with many constructor parameters
    // Static factories and constructors share a limitation: they don't scale well to large numbers of optional parameters.

    // Programmers have usually used the "telescoping constructor" pattern, which involves many constructors (See NutritionFactsTelescoping class).

    // When you want to create an instance of NutritionFactsTelescoping, you use the constructor with the shortest parameter list
    // containing all the parameters you want to set (Have to pass value 0 for unwanted values)

    NutritionFactsTelescoping cocaCola = new NutritionFactsTelescoping(240, 8, 100, 0, 35, 27);

    // A second alternative when facing many optional parameters is the JavaBeans pattern, in which you call a parameterless
    // constructor to create the object and then call setter methods to set each required parameter and each optional parameter:

    NutritionFactsJavaBeans pepsi = new NutritionFactsJavaBeans();
    pepsi.setServingSize(240);
    pepsi.setServings(8);
    pepsi.setCalories(100);
    pepsi.setSodium(35);
    pepsi.setCarbohydrate(27);

    // Because construction is split across multiple calls, a JavaBean may be in an inconsistent state partway through its construction.
    // Also, the use of JavaBeans requires making the class mutable.

    // There is a third alternative that combines the safety of the telescoping constructor pattern with the readability
    // of the JavaBeans pattern: The Builder Pattern

    NutritionFacts inkaCola = new NutritionFacts.Builder(240, 8)
        .calories(100)
        .sodium(35)
        .carbohydrate(27)
        .build();

    // The NutritionFacts class is immutable, and all parameter default values are in one place
    // The Builder pattern simulates named optional parameters (Like in C# or Kotlin)

    // The Builder pattern is well suited to class hierarchies. For example, consider an abstract class at the root
    // of a hierarchy representing various kinds of pizza:

    Pizza pizza;

    // NyPizza and Calzone are both subclasses of Pizza
    // The result for these "hierarchical builders" is a client code identical to the code for the simple
    // NutritionFacts builder. (importing enum constants statically in this example for brevity):

    NyPizza nyPizza = new NyPizza.Builder(SMALL)
        .addTopping(SAUSAGE)
        .addTopping(ONION)
        .build();

    Calzone calzone = new Calzone.Builder()
        .addTopping(HAM)
        .sauceInside()
        .build();

    // In summary, the builder pattern is a good choice when designing classes whose constructors or static factories
    // would have more than a handful of parameters
  }

  void item3() { // Enforce the singleton property with a private constructor or an enum type
    // A singleton is simply a class that is instantiated exactly once.
    // Making a class a singleton can make it difficult to test its clients because it's impossible to substitute
    // a mock implementation for a singleton unless it implements an interface that serves as its type.

    // There are two common ways to implement singletons, both keep the constructor private and export a
    // public static member to access the instance:

    // 1. Public member as final field
    Elvis1.INSTANCE.leaveTheBuilding();

    // 2. Public member as static factory method
    Elvis2.getInstance().leaveTheBuilding();

    // To make any of these two approaches Serializable, it's not sufficient merely to add "implements Serializable"
    // to its declaration.
    // To mantain the singleton guarantee, declare all instance fields transient and provide a readResolve method.
    // Otherwise, each time a serialized instance is deserialized, a new instance will be created. To prevent this from
    // happening, add the readResolve method to the Elvis2 class.

    // A third way to implement a singleton is to declare a single-element enum:
    Elvis3.INSTANCE.leaveTheBuilding();
    // This approach provides Serialization for free, and provides a guarantee against multiple instantiation.
    // May feel a bit unnatural, but this is often the best way to implement a singleton. Note that you can't use this
    // approach if your singleton must extend a superclass other than Enum (though you can declare an enum to implement
    // interfaces.
  }

  void item4() { // Enforce noninstantiability with a private constructor.
    UtilityClass.doSomething();
    // Because the explicit constructor is private, it is inaccessible outside the class.
  }

  void item5() { // Prefer dependency injection to hardwiring resources
    // Many classes depend on one or more resources. For example, a spell checker depends on a dictionary. Usually such
    // classes are implemented as static utility classes:
    SpellCheckerStatic.isValid("Pepsi");

    // Similarly, it's not uncommon to see them implemented as singletons:
    SpellCheckerSingleton.INSTANCE.suggestions("Bepsi");

    // Neither of these approaches is satisfactory, because they assume there is only one dictionary worth using.
    // There may by many different dictionaries, it may be even desirable to use a special dictionary for testing.
    // Static utility classes and singletons are inappropriate for classes whose behavior is parametrized by an
    // underlying resource.

    // What is required is the ability to support multiple instances of the class, each of which uses the resource
    // desired by the client.

    // A simple pattern that satisfies this requirement is to pass the resource into the constructor when creating
    // a new instance. This is one form of dependency injection:
    new SpellCheckerInjection(new Lexicon()).isValid("isded");

    // In summary, don't use a singleton or static utility class to implement a class that depends on one or more
    // underlying resources whose behavior affects that of the class, and do not have the class create these resources
    // directly. Instead, pass the resources, or factories to create them, into the constructor (or static factory or
    // builder. This practice, known as dependency injection, will greatly enhance the flexibility, reusability and
    // testability of a class.
  }
}