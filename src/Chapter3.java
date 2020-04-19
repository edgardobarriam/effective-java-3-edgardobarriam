/**
 * Although Object is a concrete class, it is designed primarily for extension. All of its nonfinal methods (equals,
 * hashCode, toString clone, and finalize) have explicit general contracts because they are designed to be overridden.
 * It is the responsibility of any class overriding these methods to obey their general contracts; failure to do so
 * will prevent other classes that depend on the contracts (such as HashMap and HashSet) from functioning properly in
 * conjunction with the class. This chapter tells you when and how to override the nonfinal Object methods.
 */
public class Chapter3 { // Methods Common to All Objects

  void item10() { // Obey the general contract when overriding equals
    // The easiest way to avoid problems is not to override the equals method, in which case each instance of the class
    // is equal only to itself. This is the right thing to do if any of the following conditions apply:

    // • Each instance of the class is inherently unique.
    // • There is no need for the class to provide a "logical equality" test.
    // • A superclass has already overridden equals, and the superclass behavior is appropriate for this class.
    // • The class is private or package-private, and you are certain that its equals method will never be invoked.

    // So when is it appropriate to override equals? It is then a class as a notion of logical equality that differs
    // from mere object identity and a superclass has not already overridden equals. This is generally the case for
    // value classes. A value class is simply a class that represents a value, such as Integer or String.

    // Not only is overriding the equals method necessary to satisfy programmer expectations, it enables instances to
    // serve as map keys or set elements with predictable, desirable behavior.

    // When you override the equals method, you must adhere to its general contract, from the specification for Object:

    // • Reflexive: For any non-null reference value, x.equals(x) must return true.
    // • Symmetric: For any non-null reference values x and y, x.equals(y) must return true if and only if y.equals(x)
    //              returns true.
    // • Transitive: For any non-null reference values x,y,x if x.equals(y) returns true and y.equals(z) returns true,
    //               then x.equals(z) must return true.
    // • Consistent: For any non-null reference values x and y, multiple invocations of x.equals(y) must consistently
    //               return true or consistently return false, provided no information used in equals comparisons is
    //               modified.
    // • For any non-null reference value x, x.equals(null) must return false.

    // Here are some points for a high-quality equals method:

    // 1. Use the == operator to check if the argument is a reference to this object. If so, return true. This is just a
    // performance optimization.
    // 2. Use the instanceof operator to check if the argument has the correct type. If not, return false.
    // 3. Cast the argument to the correct type. Because this cast was preceded by an instanceof test, it is guaranteed
    // to succeed.
    // 4. For each "significant" field in the class, check if that field of the argument matches the corresponding field
    // of this object. If all these tests succeed, return true; otherwise, return false.

    // When you are finished writing your equals method, adk yourself: Is it symmetric? Is it transitive? Is it
    // consistent?
  }
}
