package content;

import java.io.Serializable;

// Singleton with static factory
public class Elvis2 implements Serializable {
    private static final Elvis2 INSTANCE = new Elvis2();

    private Elvis2() {} // Replace default constructor

    public static Elvis2 getInstance() { return INSTANCE; }

    public void leaveTheBuilding() {
        System.out.println("Leaving the building");
    }

    private Object readResolve() {
        // Return the one true Elvis2 and let the gc take care of the Elvis 2 impersonator.
        return INSTANCE;
    }
}