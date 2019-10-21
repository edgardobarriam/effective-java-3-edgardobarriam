package content;

// Singleton with public final field
public class Elvis1 {
    public static final Elvis1 INSTANCE = new Elvis1();

    private Elvis1() {} // Replace the default constructor

    public void leaveTheBuilding() {
        System.out.println("Leaving the building");
    }
}