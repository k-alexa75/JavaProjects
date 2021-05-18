package Repository.RaceRepository;

import Domain.Race;
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

public class RaceRepository implements IRaceRepository{

    private JDBCUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RaceRepository(Properties properties) {
        System.out.println("Initializing RaceRepository with properties: {}"+ properties);
        this.dbUtils = new JDBCUtils(properties);
    }

    @Override
    public Iterable<Race> findAll() {
        logger.traceEntry();

        Connection connection = dbUtils.getConnection();
        List<Race> races = new ArrayList<>();
        try(PreparedStatement preStmt = connection.prepareStatement
                ("SELECT * FROM Races")){
            try(ResultSet resultSet = preStmt.executeQuery()){
                while(resultSet.next()){
                    int ID_race = resultSet.getInt("ID_race");
                    String name= resultSet.getString("name");
                    int engine_capacity = resultSet.getInt("engine_capacity");
                    int maximum_riders = resultSet.getInt("maximum_riders");

                    Race race=new Race(name, engine_capacity, maximum_riders);
                    race.setId(ID_race);
                    races.add(race);
                }
            }
        }catch (SQLException exception){
            logger.error(exception);
            System.out.println("Error DB" + exception);
        }
        logger.traceExit(races);
        return races;
    }

    @Override
    public void save(Race entity) {
        logger.traceEntry("saving task {}", entity);

        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement
                ("INSERT INTO Races (name, engine_capacity, maximum_iders) VALUES (?,?,?)")){
            preStmt.setString(1, entity.getName());
            preStmt.setInt(2, entity.getEngineCapacity());
            preStmt.setInt(3, entity.getMaximumRiders());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);

        }catch (SQLException exception){
            logger.error(exception);
            System.out.println("Error DB "+ exception);
        }
        logger.traceExit();
    }

    @Override
    public Race getRaceByEngineCapacity(int engineCapacity) {
        logger.traceEntry("Searching races with engine capacity {} ", engineCapacity);

        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement
                ("SELECT * FROM Races WHERE engine_capacity=?")){
            preStmt.setInt(1, engineCapacity);
            try(ResultSet resultSet = preStmt.executeQuery()){
                while(resultSet.next()){
                    int ID_race = resultSet.getInt("ID_race");
                    String name= resultSet.getString("name");
                    int maximum_riders = resultSet.getInt("maximum_riders");

                    Race race=new Race(name, engineCapacity, maximum_riders);
                    race.setId(ID_race);
                    return race;
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
