public interface  Dao {

    void openConnection();

    void closeConnetion();

    boolean createDatabase();

    void checkForUpdates();

} 