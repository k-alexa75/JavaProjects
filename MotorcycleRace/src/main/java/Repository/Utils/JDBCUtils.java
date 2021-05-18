package Repository.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {

    private Properties JDBCProperties;
    private static final Logger logger = LogManager.getLogger();
    private Connection instance = null;

    public JDBCUtils(Properties JDBCProperties) {
        this.JDBCProperties = JDBCProperties;
    }

    private Connection getNewConnection() {
        logger.traceEntry();

        String url = JDBCProperties.getProperty("jdbc.url");
        String user = JDBCProperties.getProperty("jdbc.user");
        String password = JDBCProperties.getProperty("jdbc.password");

        logger.info("trying to connect to database ... {}", url);
        logger.info("user: {}", user);
        logger.info("password: {}", password);

        Connection connection = null;
        try {
            if (user != null && password != null) {
                connection = DriverManager.getConnection(url, user, password);
            } else {
                connection = DriverManager.getConnection(url);
            }
        } catch (SQLException exception) {
            logger.error(exception);
            System.out.println("Error getting connection" + exception);
        }
        return connection;
    }

    public Connection getConnection(){
        logger.traceEntry();

        try{
            if (instance==null || instance.isClosed()){
                instance=getNewConnection();
            }
        }catch (SQLException exception){
            logger.error(exception);
            System.out.println("Error DB " + exception);
        }

        logger.traceExit(instance);
        return instance;
    }
}
