package Repository.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {

    private Properties JDBCProperties;
    //private static final Logger logger = LogManager.getLogger();
    private Connection instance = null;

    public JDBCUtils(Properties JDBCProperties) {
        this.JDBCProperties = JDBCProperties;
    }

    private Connection getNewConnection() {
        String driver=JDBCProperties.getProperty("race.jdbc.driver");
        System.out.println(driver);
        String url=JDBCProperties.getProperty("race.jdbc.url");
        String user=JDBCProperties.getProperty("race.jdbc.user");
        String pass=JDBCProperties.getProperty("race.jdbc.pass");
        Connection con=null;
        try {
            Class.forName(driver);
            con= DriverManager.getConnection(url,user,pass);
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading driver "+e);
        } catch (SQLException e) {
            System.out.println("Error getting connection "+e);
        }
        return con;
    }

    public Connection getConnection(){
        //logger.traceEntry();

        try{
            if (instance==null || instance.isClosed()){
                instance=getNewConnection();
            }
        }catch (SQLException exception){
            System.out.println(exception);
            System.out.println("Error DB " + exception);
        }

        //logger.traceExit(instance);
        return instance;
    }
}
