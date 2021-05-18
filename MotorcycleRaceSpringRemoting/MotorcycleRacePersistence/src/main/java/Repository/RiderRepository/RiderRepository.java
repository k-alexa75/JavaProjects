package Repository.RiderRepository;

import Domain.Rider;
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

public class RiderRepository implements IRiderRepository{

    private JDBCUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RiderRepository(Properties properties) {
        System.out.println("Initializing RiderRepository with properties: "+ properties);
        this.dbUtils = new JDBCUtils(properties);
    }

    @Override
    public Iterable<Rider> findAll() {
        logger.traceEntry();

        Connection connection = dbUtils.getConnection();
        List<Rider> riders = new ArrayList<>();
        try(PreparedStatement preStmt = connection.prepareStatement
                ("SELECT * FROM Riders")){
            try(ResultSet resultSet = preStmt.executeQuery()){
                while(resultSet.next()){
                    int ID_rider = resultSet.getInt("ID_rider");
                    String first_name = resultSet.getString("first_name");
                    String last_name = resultSet.getString("last_name");
                    String team = resultSet.getString("team");
                    int engine_capacity = resultSet.getInt("engine_capacity");

                    Rider rider = new Rider(first_name, last_name, team, engine_capacity);
                    rider.setId(ID_rider);
                    riders.add(rider);
                }
            }
        }catch (SQLException exception){
            logger.error(exception);
            System.out.println("Error DB" + exception);
        }
        logger.traceExit(riders);
        return riders;
    }

    @Override
    public void save(Rider entity) {
        logger.traceEntry("saving task {}", entity);

        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement
                ("INSERT INTO Riders (first_name, last_name, team, engine_capacity) VALUES (?,?,?,?)")){
            preStmt.setString(1, entity.getFirstName());
            preStmt.setString(2, entity.getLastName());
            preStmt.setString(3, entity.getTeam());
            preStmt.setInt(4, entity.getEngineCapacity());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);

        }catch (SQLException exception){
            logger.error(exception);
            System.out.println("Error DB "+ exception);
        }
        logger.traceExit();
    }

    @Override
    public Iterable<Rider> getRidersByTeam(String teamName) {
        logger.traceEntry("Searching riders from {} team", teamName);

        Connection connection = dbUtils.getConnection();
        List<Rider> riders = new ArrayList<>();
        try(PreparedStatement preStmt = connection.prepareStatement
                ("SELECT * FROM Riders WHERE team=?")){
            preStmt.setString(1, teamName);
            try(ResultSet resultSet = preStmt.executeQuery()){
                while(resultSet.next()){
                    int ID_rider = resultSet.getInt("ID_rider");
                    String first_name = resultSet.getString("first_name");
                    String last_name = resultSet.getString("last_name");
                    int engine_capacity = resultSet.getInt("engine_capacity");

                    Rider rider = new Rider(first_name, last_name, teamName, engine_capacity);
                    rider.setId(ID_rider);
                    riders.add(rider);
                }
            }
        }catch (SQLException exception){
            logger.error(exception);
            System.out.println("Error DB" + exception);
        }
        logger.traceExit(riders);
        return riders;
    }

    @Override
    public Rider getRiderByName(String firstName, String lastName) {
        logger.traceEntry("Searching rider {} {}", firstName, lastName);

        Connection connection = dbUtils.getConnection();
        List<Rider> riders = new ArrayList<>();
        try(PreparedStatement preStmt = connection.prepareStatement
                ("SELECT * FROM Riders WHERE first_name=? AND last_name=?")){
            preStmt.setString(1, firstName);
            preStmt.setString(2, lastName);
            try(ResultSet resultSet = preStmt.executeQuery()){
                while(resultSet.next()){
                    int ID_rider = resultSet.getInt("ID_rider");
                    String team = resultSet.getString("team");
                    int engine_capacity = resultSet.getInt("engine_capacity");

                    Rider rider = new Rider(firstName, lastName, team, engine_capacity);
                    rider.setId(ID_rider);
                    logger.traceExit(rider);
                    return rider;
                }
            }
        }catch (SQLException exception){
            logger.error(exception);
            System.out.println("Error DB" + exception);
        }
        logger.traceExit(null);

        return null;
    }
}
