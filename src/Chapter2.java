import content.*;

import java.io.*;
import java.math.BigInteger;
import java.util.EnumSet;
import java.util.Random;

import static content.NyPizza.Size.SMALL;
import static content.Pizza.Topping.*;

public class Chapter2 { // Creating and Destroying Objects

  void item1() { // Consider static factory methods instead of constructors
    // A class can provide a public static factory method. An example from Boolean:
    Boolean myBoolean = Boolean.valueOf(true);

    // (1) One advantage of static factory methods is that they have names, hence they are often more readable than
    // constructors.
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
    // Also hides implementation classes from clients, which can be helpful if there are any future changes on the
    // implementation classes .

    // (5) Another advantage of static factories is that the class of the returned object need not exist when the class
    // containing the method is written.

    // (6) The main limitation of providing only static factory methods is that classes without public or protected
    // constructors cannot be subclassed.

    // (7) Also, static factory methods are hard for programmers to find.
    // This problem can be reduced by using common naming conventions. Some examples:
    // from(), of(), valueOf(), instance() or getInstance(), create() or newInstance(),
    // get"Type"(), new"Type"(), "type"()

    // Static factories and public constructors both have their uses. Consider static factories instead of
    // public constructors.
  }

  void item2() { // Consider a builder when faced with many constructor parameters
    // Static factories and constructors share a limitation: they don't scale well to large numbers of optional
    // parameters.

    // Programmers have usually used the "telescoping constructor" pattern, which involves many constructors
    // (See NutritionFactsTelescoping class).

    // When you want to create an instance of NutritionFactsTelescoping, you use the constructor with the shortest
    // parameter list
    // containing all the parameters you want to set (Have to pass value 0 for unwanted values)

    NutritionFactsTelescoping cocaCola = new NutritionFactsTelescoping(
        240,
        8,
        100,
        0,
        35,
        27);

    // A second alternative when facing many optional parameters is the JavaBeans pattern, in which you call a
    // parameterless constructor to create the object and then call setter methods to set each required parameter and
    // each optional parameter:

    NutritionFactsJavaBeans pepsi = new NutritionFactsJavaBeans();
    pepsi.setServingSize(240);
    pepsi.setServings(8);
    pepsi.setCalories(100);
    pepsi.setSodium(35);
    pepsi.setCarbohydrate(27);

    // Because construction is split across multiple calls, a JavaBean may be in an inconsistent state partway through
    // its construction. Also, the use of JavaBeans requires making the class mutable.

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
    // To maintain the singleton guarantee, declare all instance fields transient and provide a readResolve method.
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
    new SpellCheckerInjection(new Lexicon("AnyLanguage")).isValid("isded");

    // In summary, don't use a singleton or static utility class to implement a class that depends on one or more
    // underlying resources whose behavior affects that of the class, and do not have the class create these resources
    // directly. Instead, pass the resources, or factories to create them, into the constructor (or static factory or
    // builder). This practice, known as dependency injection, will greatly enhance the flexibility, reusability and
    // testability of a class.
  }

  void item6() { // Avoid creating unnecessary objects
    // It is often appropriate to use a single object instead of creating a new functionally equivalent object each
    // time it is needed. An object can always be reused if it is immutable.

    // As an extreme example of what not to do, consider this statement:
    String s = new String("bikini"); // DON'T DO THIS
    // The statement creates a new String each time it is executed, and none of those object creations is necessary.
    // The argument to the String constructor is itself a String instance.

    // The improved version is:
    String str = "bikini";

    // This version uses a single String instance, rather than creating a new one each time it is executed. Also, it is
    // guaranteed that the object will be reused by any other code running in the same virtual machine that happens to
    // contain the same string literal.

    // You can often avoid creating unnecessary objects by using static factory methods in preference to constructors on
    // immutable classes that provide both. For example, the static factory method Boolean.valueOf(String) is preferable
    // to the constructor Boolean(String). The constructor must create a new object each time it's called, while the
    // factory method is never required to do so and won't in practice. In addition to reusing immutable objects you can
    // also reuse mutable objects if you know they won't be modified.

    // If you are going to need an "expensive object" repeatedly, it may be advisable to cache it for reuse. Suppose
    // you want to write a method to determine whether a string is a Roman numeral. The easiest way to do so using
    // regular expressions is:

    RomanNumerals.isRomanNumeral("XIV");

    // The problem with this implementation is that it relies on the String.matches method. While String.matches is the
    // easiest way to check if a string matches a regular expression, it's not suitable for repeated use in performance-
    // critical situations. It internally creates a Pattern instance for the regex and uses it only once, after which
    // it becomes eligible for garbage collection. Creating a Pattern instance is expensive because it requires
    // compiling the regex into a finite state machine.

    // To improve performance, compile the regex into a Pattern instance (which is immutable) as part of class
    // initialization, cache it, and reuse the same instance for every invocation of the method:

    RomanNumeralsImproved.isRomanNumeral("XVI");

    // The improved version provides significant performance gains if invoked frequently (Approximately 6.5 times
    // faster).

    // Another way to create unnecessary objects is autoboxing, which allows the programmer to mix primitive and boxed
    // primitive types, boxing and unboxing automatically as needed.
    // Autoboxing blurs but does not erase the distinction between primitive and boxed primitive types. Consider:

    // Hideously slow! Can you spot the object creation?
    Long sum = 0L;
    for (long i = 0; i <= Integer.MAX_VALUE; i++) {
      sum += i;
    }

    // The variable sum is declared as a Long instead of a long, which means that the programs constructs about 2^31
    // unnecessary Long instances. Changing the declaration of sum from Long to long reduced the runtime from 6.3s to
    // 0.59s. The lesson is clear: prefer primitives to boxed primitives, and watch out for unintentional autoboxing.

    // You should not conclude that object creation is expensive and should be avoided. On the contrary, the creation
    // and reclamation of small objects whose constructors do little explicit work is cheap, especially on modern JVM
    // implementations. Creating additional objects to enhance readability or simplicity of a program is generally a
    // good thing.

    // Conversely, avoiding object creation by maintaining your own object pool is a bad idea unless the objects in the
    // pool are extremely heavyweight. An example of an object that justifies and object pool is a database connection.
  }

  void item7() { // Eliminate obsolete object references
    // Consider the following simple stack implementation:
    Stack stack;

    // There is nothing obviously wrong with this program, but it has a "memory leak" which can silently manifest itself
    // as reduced performance due to increased garbage collector activity or increased memory footprint.
    // In extreme cases, such memory leaks can cause disk paging and even program failure with an OutOfMemoryError, but
    // such failures are relatively rare.

    // Problem is, the objects that were popped off the stack won't be garbage collected, even if the program using the
    // stack has no more references to them. This happens because the stack maintains obsolete references to these
    // objects. In this case, any references outside of the "active portion" of the element array are obsolete. The
    // active portion consists of the elements whose index is less than size.
    // If and object reference is unintentionally retained, not only is that object excluded from garbage collection,
    // but so too are any objects referenced by that object, and so on.

    // The fix for this problem is simple: null out the references once they become obsolete. (See improvement)

    // Nulling out object references should be the exception rather than the norm. The best way to eliminate an obsolete
    // reference is to let the variable that contained the reference fall out of scope. This occurs naturally, if you
    // define each variable in the narrowest possible scope.

    // What aspect of the Stack class makes it susceptible to memory leaks? Simply put, it manages its own memory.
    // Generally speaking, whenever a class manages its own memory, the programmer should be alert for memory leaks.
    // Whenever an element is freed, any object references contained in the element should be nulled out.

    // Another common source of memory leaks is caches. There are several solutions in this case:

    // If the cache entries are relevant exactly as long as there are references to its key outside of the cache,
    // represent the cache as a WeakHashMap; entries will be removed automatically after they become obsolete.
    // Remember that WeakHashMap is useful only if the desired lifetime if cache entries is determined by external
    // references to the key, not the value.

    // More commonly, the useful lifetime of a cache entry is less well defined, with entries becoming less valuable
    // over time. In this case, the cache should be occasionally cleansed of entries that have fallen into disuse,
    // maybe by using a background thread or as a side effect of adding new entries to the cache.

    // A third common source of memory leaks is listeners and other callbacks.
    // If you implement an API where clients register callbacks but don't deregister then explicitly, they will
    // accumulate unless you take some action. One way to ensure that callbacks are garbage collected is to store only
    // weak references to then, for instance, by storing them only as keys in a WeakHashMap.
  }

  void item8() { // Avoid finalizers and cleaners
    // Finalizers are unpredictable, often dangerous and generally unnecessary.
    // Their use can cause erratic behavior, poor performance and portability problems. The Java 9 replacement for
    // finalizers is cleaners. Cleaners are less dangerous than finalizers but still unpredictable, slow, and generally
    // unnecessary.

    // One shortcoming of finalizers and cleaners is that there is no guarantee they'll be executed promptly. This means
    // that you should never do anything time-critical in a finalizer or cleaner.
    // For example, it's a grave error to depend on a finalizer or cleaner to close files because open file descriptors
    // are a limited resource. If many files are left open as a result of the system's tardiness in running finalizers
    // or cleaners, a program may fail because it can no longer open files.

    // Not only does the specification provide no guarantee that finalizers or cleaners will run promptly; it provides
    // no guarantee that they will run at all. As a consequence, you should never depend on a finalizer or cleaner to
    // update persistent state. For example, depending on a finalizer or cleaner to release a persistent lock on a
    // shared resource such as a database is a terrible idea.

    // So what should you do instead of writing a finalizer or cleaner for a class whose objects encapsulate resources
    // that require termination, such as files or threads? Just have your class implement AutoCloseable, and require its
    // clients to invoke the close method on each instance when it is no longer needed, typically using
    // try-with-resources to ensure termination even in the face of exceptions. It is worth mentioning that the instance
    // must keep tracks of whether it has been closed: the close() method must record in a field that the object is no
    // longer valid, and other methods must check this field and throw an IllegalStateException if they are called after
    // the object has been closed.

    // In summary, don't use cleaners, or in release prior to Java 9, finalizers, except as a safety net or to terminate
    // noncritical native resources. Even then, beware the indeterminacy and performance consequences.
  }

  void item9() throws IOException { // Prefer try-with-resources to try-finally
    // The Java libraries include many resources that must be closed manually by invoking a close method. Examples
    // include InputStream, OutputStream and java.sql.Connection. Closing resources is often overlooked by clients,
    // with predictably dire performance consequences. While many of these resources use finalizers as a safety net,
    // we know finalizers don't work very well.

    // Originally, a try-finally statement was the best way to guarantee that a resource would be closed properly,
    // even in the face of an exception or return:

    String fakePath = "/tmp/file.txt";
    BufferedReader br = new BufferedReader(new FileReader(fakePath));
    // try-finally is no longer the best way to close resources
    try {
      br.readLine();
    } finally {
      br.close();
    }

    // Java 7 introduced the try-with-resources statement. To be usable with this construct, a resource must implement
    // the AutoCloseable interface, which consists og a single void-returning close method. Many classes and interfaces
    // in the Java libraries and in third-party libraries now implement or extend AutoCloseable.
    // An example:

    // try-with-resources - the best way to close resources!
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fakePath))) {
      br.readLine();
    }

    // It can handle multiple resources:
    try (InputStream in = new FileInputStream(fakePath); OutputStream out = new FileOutputStream(fakePath)) {
      byte[] buffer = new byte[128];
      int n;
      while ((n = in.read(buffer)) >= 0) {
        out.write(buffer, 0 , n);
      }
    }

    // The lesson is clear: Always use try-with-resources in preference to try-finally when working with resources that
    // must be closed. The resulting code is shorter and clearer, and the exception that it generates are more useful.
  }
}