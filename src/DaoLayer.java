// This is in detached head state
public class DaoLayer () {
    public static void main (String[] args) {
        sayHelloWithName("Nayan", "Mr.");
    }

    private static void sayHelloWithName(String name, String title) {
        System.out.println("Hello " + title + " " + name);
    }
}