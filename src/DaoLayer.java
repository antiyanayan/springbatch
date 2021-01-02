// This is in detached head state
public class DaoLayer implements Dao {

    public void openConnection() {
        // open the connecton
    }

    public void closeConnection() {
        //close the connection
    }

    public static void main (String[] args) {
        sayHelloWithName("Nayan", "Mr.");
    }

    private static void sayHelloWithName(String name, String title) {
        System.out.println("Hello " + title + " " + name);
    }
}