package Repository.RegistrationRepository;

import Domain.Registration;
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

public class RegistrationRepository implements IRegistrationRepository{

    private JDBCUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RegistrationRepository(Properties properties) {
        logger.info("Initializing RegistrationRepository with properties: {}", properties);
        this.dbUtils = new JDBCUtils(properties);
    }

    @Override
    public Iterable<Registration> findAll() {
        logger.traceEntry();

        Connection connection = dbUtils.getConnection();
        List<Registration> registrations = new ArrayList<>();
        try(PreparedStatement preStmt = connection.prepareStatement
                ("SELECT * FROM Registrations")){
            try(ResultSet resultSet = preStmt.executeQuery()){
                while(resultSet.next()){
                    int ID_registration = resultSet.getInt("ID_registration");
                    int rider_id = resultSet.getInt("rider_id");
                    int race_id = resultSet.getInt("race_id");

                    Registration registration = new Registration(rider_id, race_id);
                    registration.setId(ID_registration);
                    registrations.add(registration);
                }
            }
        }catch (SQLException exception){
            logger.error(exception);
            System.out.println("Error DB" + exception);
        }
        logger.traceExit(registrations);
        return registrations;
    }

    @Override
    public void save(Registration entity) {
        logger.traceEntry("saving task {}", entity);

        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement
                ("INSERT INTO Registrations (rider_id, race_id) VALUES (?,?)")){
            preStmt.setInt(1, entity.getRiderID());
            preStmt.setInt(2, entity.getRaceID());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);

        }catch (SQLException exception){
            logger.error(exception);
            System.out.println("Error DB "+ exception);
        }
        logger.traceExit();
    }
}
