package Repository.UserRepository;

import Domain.User;
import Repository.Utils.JDBCUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserRepository implements IUserRepository{

    private JDBCUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public UserRepository(Properties properties) {
        logger.info("Initializing UserRepository with properties: {}", properties);
        this.dbUtils = new JDBCUtils(properties);
    }


    @Override
    public Iterable<User> findAll() {

        logger.traceEntry("Searching all Users...");

        Connection connection = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try(PreparedStatement preStmt = connection.prepareStatement
                ("SELECT * FROM Users")){
            try(ResultSet resultSet = preStmt.executeQuery()){
                while(resultSet.next()){
                    int ID_user = resultSet.getInt("ID_user");
                    String first_name = resultSet.getString("first_name");
                    String last_name = resultSet.getString("last_name");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    User user = new User(first_name, last_name, username, password);
                    user.setId(ID_user);
                    users.add(user);
                }
            }
        }catch (SQLException exception){
            logger.error(exception);
            System.out.println("Error DB" + exception);
        }
        logger.traceExit(users);
        return users;
    }

    @Override
    public void save(User entity) {

        logger.traceEntry("saving task {}", entity);

        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement
                ("INSERT INTO Users (first_name, last_name, username, password) VALUES (?,?,?,?)")){
            preStmt.setString(1, entity.getFirstName());
            preStmt.setString(2, entity.getLastName());
            preStmt.setString(3, entity.getUsername());
            preStmt.setString(4, entity.getPassword());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);

        }catch (SQLException exception){
            logger.error(exception);
            System.out.println("Error DB "+ exception);
        }
        logger.traceExit();
    }


    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        logger.traceEntry();

        Connection connection = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try(PreparedStatement preStmt = connection.prepareStatement
                ("SELECT * FROM Users WHERE username=? AND password=?")){
            preStmt.setString(1, username);
            preStmt.setString(2, password);
            try(ResultSet resultSet = preStmt.executeQuery()){
                while(resultSet.next()){
                    int ID_user = resultSet.getInt("ID_user");
                    String first_name = resultSet.getString("first_name");
                    String last_name = resultSet.getString("last_name");

                    User user = new User(first_name, last_name, username, password);
                    user.setId(ID_user);
                    return user;
                }
            }
        }catch (SQLException exception){
            logger.error(exception);
            System.out.println("Error DB" + exception);
        }
        logger.traceExit();
        return null;
    }
}
